package uk.ac.ucl.model.io;

public class DataLoadException extends RuntimeException {
    public DataLoadException(String message, String filename) {
        super(message + filename);
    }

    public DataLoadException(String message, String filename, Throwable cause) {
        super(message + filename, cause);
    }
}

