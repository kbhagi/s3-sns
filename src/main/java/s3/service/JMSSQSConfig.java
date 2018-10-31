package s3.service;

import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;



/**
 * Created by root on 31/10/18.
 */
public class JMSSQSConfig {

    @Value("${sqs.queue.endpoint}")
    private String endpoint;

    @Value("${sqs.queue.name}")
    private String queueName;

    @Autowired
    private SQSListener sqsListener;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Bean
    public DefaultMessageListenerContainer jmsListenerContainer() {

        SQSConnectionFactory sqsConnectionFactory = SQSConnectionFactory.builder()
                .withAWSCredentialsProvider(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey,secretKey)))
                .withEndpoint(endpoint)
                .withAWSCredentialsProvider(awsCredentialsProvider)
                .withNumberOfMessagesToPrefetch(10).build();

        DefaultMessageListenerContainer dmlc = new DefaultMessageListenerContainer();
        dmlc.setConnectionFactory(sqsConnectionFactory);
        dmlc.setDestinationName(queueName);

        dmlc.setMessageListener(sqsListener);

        return dmlc;
    }

    @Bean
    public JmsTemplate createJMSTemplate() {

        SQSConnectionFactory sqsConnectionFactory = SQSConnectionFactory.builder()
                .withAWSCredentialsProvider(awsCredentialsProvider)
                .withEndpoint(endpoint)
                .withNumberOfMessagesToPrefetch(10).build();

        JmsTemplate jmsTemplate = new JmsTemplate(sqsConnectionFactory);
        jmsTemplate.setDefaultDestinationName(queueName);
        jmsTemplate.setDeliveryPersistent(false);


        return jmsTemplate;
    }

    private final AWSCredentialsProvider awsCredentialsProvider = new AWSCredentialsProvider() {
        @Override
        public AWSCredentials getCredentials() {
            return new BasicAWSCredentials(accessKey, secretKey);
        }

        @Override
        public void refresh() {

        }
    };

}

