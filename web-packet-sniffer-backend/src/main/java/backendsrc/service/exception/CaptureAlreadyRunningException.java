package backendsrc.service.exception;

public class CaptureAlreadyRunningException extends RuntimeException {
    public CaptureAlreadyRunningException() {
        super("Capture session already running");
    }
}
