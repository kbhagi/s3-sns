package s3.service;

import com.amazonaws.*;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.mediastoredata.model.GetObjectResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Created by root on 23/10/18.
 */
@Service
public class S3OperationsServiceImpl implements S3OperationsService {

    @Autowired
    private AmazonS3 s3;


    @org.springframework.beans.factory.annotation.Value("${aws.s3.bucket}")
    private String bucketName;

    private static final Logger LOGGER = Logger.getLogger(S3OperationsServiceImpl.class.getName());


    @Override
    public String GenerateUrlToDownload(String bucketName, String key) {
        if ((bucketName != null && !bucketName.trim().isEmpty()) && (key != null & !key.trim().isEmpty())) {
            String file = Paths.get(key).getFileName().toString();
            String toBeReturned = null;
            try {
                LOGGER.info(s3.toString());
//            AccessControlList acl = new AccessControlList();
//            acl.grantPermission(GroupGrantee.AllUsers, Permission.FullControl);
//            S3Object object = s3.getObject(bucketName, key);
//            LOGGER.info(object.toString());
                s3.getObject(new GetObjectRequest(bucketName, key));
//            s3client.setObjectAcl(bucketName, keyObject, CannedAccessControlList.Private);

// Generate URL
                System.out.println("Generating pre-signed URL.");
                java.util.Date expiration = new java.util.Date();
                long milliSeconds = expiration.getTime();
                milliSeconds += 100 * 60 * 60; // Add 1 hour.
                expiration.setTime(milliSeconds);
                GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key);
                generatePresignedUrlRequest.setMethod(HttpMethod.GET);
                generatePresignedUrlRequest.setExpiration(expiration);
                URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
                toBeReturned = url.toString();
                System.out.println("Pre-Signed URL = " + toBeReturned);
                return toBeReturned;

            } catch (AmazonServiceException e) {
                // The call was transmitted successfully, but Amazon S3 couldn't process
                // it, so it returned an error response.
                e.printStackTrace();
            } catch (SdkClientException e) {
                // Amazon S3 couldn't be contacted for a response, or the client
                // couldn't parse the response from Amazon S3.
                e.printStackTrace();
            }
            return toBeReturned;
        }
        return "bucketName or key is empty";
    }

    @Override
    public String GenerateUrlToUpload(String bucketName, String key, String tag) {
        String uploadUrl = null;
        if ((bucketName != null && !bucketName.trim().isEmpty()) && (key != null & !key.trim().isEmpty()) && (tag != null & !tag.trim().isEmpty())) {
            try {

                // Generate URL
                LOGGER.info("Generating pre-signed URL.");
                java.util.Date expiration = new java.util.Date();
                long milliSeconds = expiration.getTime();
                milliSeconds += 100 * 60 * 60; // Add 1 hour.
                expiration.setTime(milliSeconds);
                GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key);
                generatePresignedUrlRequest.setMethod(HttpMethod.PUT);
                generatePresignedUrlRequest.setExpiration(expiration);
                generatePresignedUrlRequest.putCustomRequestHeader(Headers.S3_TAGGING, tag);
                URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
                uploadUrl = url.toString();
                LOGGER.info("Pre-Signed URL = " + uploadUrl);
                return uploadUrl;
            } catch (AmazonServiceException e) {
                // The call was transmitted successfully, but Amazon S3 couldn't process
                // it, so it returned an error response.
                e.printStackTrace();

            } catch (SdkClientException e) {
                // Amazon S3 couldn't be contacted for a response, or the client
                // couldn't parse the response from Amazon S3.
                e.printStackTrace();
            }
            ;
        }
        return uploadUrl;
    }


}