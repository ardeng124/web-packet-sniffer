package backendsrc.service.exception;

public class CaptureOperationFailedException extends RuntimeException{
    public CaptureOperationFailedException(String message) {
        super(message);
    }
    
}
