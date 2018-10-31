package s3.service;

import ch.qos.logback.classic.BasicConfigurator;
import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.util.Base64;
import org.apache.logging.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;


/**
 * Created by root on 30/10/18.
 */
public class AsyncMessageReceiver {
        static Logger logger = LoggerFactory.getLogger(AsyncMessageReceiver.class);

        private static String accessKey = "AKIAIX542LKWRT4CQCJA";
        private static String secretKey = "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";


    static AWSCredentials credentials = new BasicAWSCredentials(
            accessKey,
            secretKey);


    public static void ensureQueueExists(SQSConnection connection, String queueName) throws JMSException {
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

        /**
         * In most cases, you can do this with just a createQueue call, but GetQueueUrl
         * (called by queueExists) is a faster operation for the common case where the queue
         * already exists. Also many users and roles have permission to call GetQueueUrl
         * but don't have permission to call CreateQueue.
         */
        if( !client.queueExists(queueName) ) {
            client.createQueue( queueName );
        }
    }


    public static void handleMessage(Message message) throws JMSException {
       logger.info( "Got message " + message.getJMSMessageID() );
        logger.info( "Content: ");
        if( message instanceof TextMessage ) {
            TextMessage txtMessage = ( TextMessage ) message;
            logger.info( "\t" + txtMessage.getText() );
        } else if( message instanceof BytesMessage ){
            BytesMessage byteMessage = ( BytesMessage ) message;
            // Assume the length fits in an int - SQS only supports sizes up to 256k so that
            // should be true
            byte[] bytes = new byte[(int)byteMessage.getBodyLength()];
            byteMessage.readBytes(bytes);
            logger.info( "\t" +  Base64.encodeAsString( bytes ) );
        } else if( message instanceof ObjectMessage ) {
            ObjectMessage objMessage = (ObjectMessage) message;
            logger.info( "\t" + objMessage.getObject() );
        }
    }
    public static void main(String[]args) throws JMSException, InterruptedException {


        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        SQSConnectionFactory connectionFactory =
                SQSConnectionFactory.builder()
                        .withRegion(Region.getRegion(Regions.AP_SOUTH_1))
                        .withAWSCredentialsProvider(new AWSStaticCredentialsProvider(credentials))
                        .build();
// Create the connection.
        SQSConnection connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        ensureQueueExists(connection, "s3-put-object-notify-message")   ;
        MessageConsumer consumer = session.createConsumer(session.createQueue("s3-put-object-notify-message"));
        ReceiverCallback callback = new ReceiverCallback();
        consumer.setMessageListener(callback);
        connection.start();

        callback.waitForOneMinuteOfSilence();
        logger.info( "Returning after one minute of silence" );

        // Close the connection. This closes the session automatically
        connection.close();
        logger.info( "Connection closed" );

    }




private static class ReceiverCallback implements MessageListener {
    // Used to listen for message silence
    private volatile long timeOfLastMessage = System.nanoTime();

    public void waitForOneMinuteOfSilence() throws InterruptedException {
        for(;;) {
            long timeSinceLastMessage = System.nanoTime() - timeOfLastMessage;
            long remainingTillOneMinuteOfSilence =
                    TimeUnit.MINUTES.toNanos(1) - timeSinceLastMessage;
            if( remainingTillOneMinuteOfSilence < 0 ) {
                break;
            }
            TimeUnit.NANOSECONDS.sleep(remainingTillOneMinuteOfSilence);
        }
    }


    @Override
    public void onMessage(Message message) {
        try {
            handleMessage(message);
            message.acknowledge();
            logger.info( "Acknowledged message " + message.getJMSMessageID() );
            timeOfLastMessage = System.nanoTime();
        } catch (JMSException e) {
            System.err.println( "Error processing message: " + e.getMessage() );
            e.printStackTrace();
        }
    }
}}

