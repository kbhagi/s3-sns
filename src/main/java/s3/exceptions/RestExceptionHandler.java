package s3.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import s3.dto.ErrorMessageDto;


/**
 * RestExceptionHandler is Exception Advice controller
 * @author Icarat1
 *
 */



@ControllerAdvice
public class RestExceptionHandler {

    /**
     *
     * @param e
     * @return
     */

    @ExceptionHandler(HandleAmazonClientException.class)
    public ResponseEntity<?> handleAmazonClientException(HandleAmazonClientException e) {
        return new ResponseEntity<ErrorMessageDto>(new ErrorMessageDto(
                e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     *
     * @param e
     * @return
     */


    @ExceptionHandler(HandleAmazonServiceException.class)
    public ResponseEntity<?> handleAmazonServiceException(HandleAmazonServiceException e) {
        return new ResponseEntity<ErrorMessageDto>(new ErrorMessageDto(
                e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Conflict.class)
    public ResponseEntity<?> handleConflictCase(Conflict e) {
        return new ResponseEntity<ErrorMessageDto>(new ErrorMessageDto(
                e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<?> handleDataNotFoundException(DataNotFoundException e) {
        return new ResponseEntity<ErrorMessageDto>(new ErrorMessageDto(
                e.getMessage()), HttpStatus.NOT_FOUND);
    }

}
