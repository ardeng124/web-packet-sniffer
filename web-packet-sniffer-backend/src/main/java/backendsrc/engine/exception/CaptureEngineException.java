package backendsrc.engine.exception;

public class CaptureEngineException extends RuntimeException {
    public Exception internalException;

    public CaptureEngineException(String message, Exception e) {
        super(message,e);
    }
    
    public CaptureEngineException(String message) {
        super(message);
    }
}
