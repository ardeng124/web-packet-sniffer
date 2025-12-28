package backendsrc.engine.exception;

public class CaptureEngineException extends RuntimeException {
    public CaptureEngineException(String message, Exception e) {
        super(message,e);
    }
}
