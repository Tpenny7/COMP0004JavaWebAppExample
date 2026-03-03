package uk.ac.ucl.model;

public class ColumnNotFoundException extends RuntimeException {
    public ColumnNotFoundException(String columnName) {
        super("Column not found: " + columnName);
    }
}
