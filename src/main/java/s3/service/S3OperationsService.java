package s3.service;

/**
 * Created by root on 24/10/18.
 */
public interface S3OperationsService {
    String  GenerateUrlToDownload(String bucketName,String key);
    String  GenerateUrlToUpload(String bucketName,String key,String tag);

}
