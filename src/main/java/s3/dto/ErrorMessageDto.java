package s3.dto;

public class ErrorMessageDto {

    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorMessageDto(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

}
