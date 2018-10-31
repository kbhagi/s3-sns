package s3.service;

/**
 * Created by root on 26/10/18.
 */


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import com.amazonaws.services.sqs.model.*;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.apache.catalina.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import s3.controller.S3Controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQSOperationsServiceImpl implements SQSOperationsService {
    static Logger logger = LoggerFactory.getLogger(SQSOperationsServiceImpl.class);

    private static String accessKey = "AKIAIX542LKWRT4CQCJA";
    private static String secretKey = "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";
    private static String myQueueUrl = "https://sqs.ap-south-1.amazonaws.com/524194408959/s3-put-object-notify-message";


  static   AWSCredentials credentials = new BasicAWSCredentials(
            accessKey,
            secretKey);
    //Create client

   static AmazonSQS sqsClient = AmazonSQSClient.builder()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.AP_SOUTH_1)
            .build();

    public static void main(String[] args) {
       logger.info(readAndProcessMessages(myQueueUrl,sqsClient).toString());


    }



    public static List readAndProcessMessages(String myQueueUrl,AmazonSQS sqsClient) {
        List<String> messagelist = new ArrayList<>();

        try {

            boolean flag = true;

            while(flag)
            {
                ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
                receiveMessageRequest.setMaxNumberOfMessages(10);
                receiveMessageRequest.withMaxNumberOfMessages(10).withWaitTimeSeconds(20);
                List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).getMessages();

                for (Message message : messages)
                {
                    //   System.out.println("    Body:          " + message.getBody());
                    messagelist.add( message.getBody());

                    String messageReceiptHandle = message.getReceiptHandle();
                    sqsClient.deleteMessage(new DeleteMessageRequest().withQueueUrl(myQueueUrl).withReceiptHandle(messageReceiptHandle));
                }
                if(messages.size()==0)
                {
                    flag = false;
                }
            }


        } catch(final AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means " +
                    "your request made it to Amazon SQS, but was " +
                    "rejected with an error response for some reason.");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (final AmazonClientException ace) {
            logger.info("Caught an AmazonClientException, which means " +
                    "the client encountered a serious internal problem while " +
                    "trying to communicate with Amazon SQS, such as not " +
                    "being able to access the network.");
            logger.info("Error Message: " + ace.getMessage());
        }
        finally {
            return messagelist;
        }
    }


}




