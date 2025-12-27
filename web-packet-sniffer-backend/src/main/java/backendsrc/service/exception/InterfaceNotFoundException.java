package backendsrc.service.exception;

public class InterfaceNotFoundException extends RuntimeException {
    public InterfaceNotFoundException(String interfaceName) {
        super("Network interface '" + interfaceName + "' not found");
    }
}