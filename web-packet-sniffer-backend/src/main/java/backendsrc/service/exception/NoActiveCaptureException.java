package backendsrc.service.exception;

public class NoActiveCaptureException extends RuntimeException {
    public NoActiveCaptureException() {
        super ("No active capture session");
    }
    
}
