package common;

public class SystemRuntimeException extends RuntimeException {

    public SystemRuntimeException(String message) {
        super(message);
    }

    public SystemRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
