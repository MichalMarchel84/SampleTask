package model;

//Data transfer object for returning error information
public class ErrorMessage {

    private String error;

    public ErrorMessage(String error) {
        this.error = error;
    }

    //Required by Jackson ObjectMapper
    public String getError() {
        return error;
    }
}
