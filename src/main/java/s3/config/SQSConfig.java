package s3.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by root on 30/10/18.
 */
@Configuration
public class SQSConfig {
    @Value("${sqs.queue.endpoint}")
    private String endpoint;
    @Value("${sqs.queue.name}")
    private String queueName;
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;


    @Bean
    public AmazonSQS createSQSClient() {

        AmazonSQS amazonSQSClient = AmazonSQSClient.builder()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey,secretKey)))
                .withRegion(Regions.AP_SOUTH_1)
                .build();
        return  amazonSQSClient;
    }
}