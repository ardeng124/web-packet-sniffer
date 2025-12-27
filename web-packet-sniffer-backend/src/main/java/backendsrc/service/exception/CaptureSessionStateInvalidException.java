package backendsrc.service.exception;

public class CaptureSessionStateInvalidException extends RuntimeException {
    public CaptureSessionStateInvalidException(String message) {
        super(message);
    }
}
