package uk.ac.ucl.model;

public class DuplicateColumnException extends RuntimeException {
    public DuplicateColumnException(String columnName) {
        super("Duplicate column name: " + columnName);
    }
}
