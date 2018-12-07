package s3.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

/**
 * Created by root on 24/10/18.
 */
@Configuration
public class S3Configuration {
    private static final Logger LOGGER = Logger.getLogger(S3Configuration.class.getName());

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.region}")
    private String region;



    @Bean
    public BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Bean
    public AmazonS3 s3client() {

        ClientConfiguration clientConfig = new ClientConfiguration();
        clientConfig.setProtocol(Protocol.HTTP);
        AmazonS3 s3Client = AmazonS3Client.builder()
                .withClientConfiguration(clientConfig)
                .withRegion(Regions.AP_SOUTH_1)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withPathStyleAccessEnabled(true).build();

        LOGGER.info(s3Client.toString());
        return s3Client;
    }

    }

