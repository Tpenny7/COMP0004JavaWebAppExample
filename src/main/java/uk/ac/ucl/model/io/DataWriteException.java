package uk.ac.ucl.model.io;

public class DataWriteException extends RuntimeException {
    public DataWriteException(String message) {
        super(message);
    }

    public DataWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
