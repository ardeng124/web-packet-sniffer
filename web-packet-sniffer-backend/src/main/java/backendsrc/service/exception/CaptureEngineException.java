package backendsrc.service.exception;

public class CaptureEngineException extends RuntimeException {
    public CaptureEngineException(String message, Exception e) {
        super(message,e);
    }
}
