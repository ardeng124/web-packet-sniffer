package backendsrc.api;

public class ApiErrorResponse {
    public int status;
    public String error; 
    public String message;
    
    public ApiErrorResponse(int statusIn, String errorIn, String messageIn){
        status = statusIn;
        error = errorIn;
        message = messageIn;    
    }

}