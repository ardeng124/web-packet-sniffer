package backendsrc.engine.exception;

public class InterfaceNotFoundException extends RuntimeException {
    public InterfaceNotFoundException(String interfaceName) {
        super("Network interface '" + interfaceName + "' not found");
    }
}