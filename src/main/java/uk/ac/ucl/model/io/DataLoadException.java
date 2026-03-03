package uk.ac.ucl.model.io;

public class DataLoadException extends RuntimeException {
    public DataLoadException(String message) {
        super(message);
    }

    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}

