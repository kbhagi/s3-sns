package s3.service;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

public class AmazonSNSClient {
   private static String accessKey = "AKIAIX542LKWRT4CQCJA";
    private static String secretKey = "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";
    AWSCredentials credentials = new BasicAWSCredentials(
            accessKey,
            secretKey);
    AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(
                    Regions.AP_SOUTH_1)
            .build();


}

