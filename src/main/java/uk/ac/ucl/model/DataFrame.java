package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class DataFrame implements Iterable<Column> {
    private final ArrayList<Column> columns = new ArrayList<>();

    public void addColumn(String name) {
        if (hasColumn(name)) throw new DuplicateColumnException(name);
        columns.add(new Column(name));
    }

    private boolean hasColumn(String columnName) {
        if (columnName == null || columnName.isBlank()) throw new IllegalArgumentException(columnName);
        for (Column column : columns) {
            if (columnName.equals(column.getName())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getColumnNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Column column : columns) {
            names.add(column.getName());
        }
        return names;
    }

    @Override
    public Iterator<Column> iterator() {
        return Collections.unmodifiableList(columns).iterator();
    }

    public Column getColumn(String columnName) {
        if (columnName == null || columnName.isBlank()) throw new IllegalArgumentException(columnName);
        for (Column column : columns) {
            if (columnName.equals(column.getName())) {
                return column;
            }
        }
        throw new ColumnNotFoundException(columnName);
    }

    public int getRowCount() {
        return columns.isEmpty() ? 0 : columns.getFirst().getSize();
    }

    public String getValue(String columnName, int row) {
        return getColumn(columnName).getRowValue(row);
    }

    public void putValue(String columnName, int row, String value) {
        getColumn(columnName).setRowValue(row, value);
    }

    public void addValue(String columnName, String value) {
        getColumn(columnName).addRowValue(value);
    }
}
