package s3.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s3.service.S3OperationsService;
import s3.service.S3OperationsServiceImpl;

/**
 * Created by root on 24/10/18.
 */
@RestController
@RequestMapping(path="/generateUrl")
public class S3Controller {
    Logger logger = LoggerFactory.getLogger(S3Controller.class);

    @Autowired
    private S3OperationsService s3;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.bucket.path}")
     private String directory;



    @GetMapping(path = "/getObject/key/{key}")
    public ResponseEntity<String> getPresignedUrl(@PathVariable("key") String key) {

        String url = s3.GenerateUrlToDownload(bucketName, directory+key);

        return new ResponseEntity<String>(url, HttpStatus.OK);
    }

    @GetMapping(path="/putObject/key/{key}")
    public ResponseEntity<String> getPreseignedUrl(@PathVariable("key") String key,@RequestHeader("tag") String tag,@RequestHeader("Content-Type") String contentType) {
        String url = s3.GenerateUrlToUpload(bucketName,directory+key,tag,contentType);
        return new ResponseEntity<String>(url,HttpStatus.OK);
    }



}
