package ahodanenok.di.exception;

public abstract class DIBaseException extends RuntimeException {

    public DIBaseException() { }

    public DIBaseException(String message) {
        super(message);
    }

    public DIBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DIBaseException(Throwable cause) {
        super(cause);
    }
}
