package uk.ac.ucl.model.io;

import uk.ac.ucl.model.DataFrame;
import uk.ac.ucl.model.DuplicateColumnException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DataLoader {
    DataFrame dataFrame;

    public DataFrame getDataFrame(){return dataFrame;}

    public void importData(String fileName) throws FileNotFoundException {
        dataFrame = new DataFrame();
        FileInput file;
        try {
            file = new FileInput(fileName);
            String[] columnNames;
            if (!file.hasNext()) throw new DataLoadException("Empty file: ", fileName);
            columnNames = file.nextLine().split(",", -1);
            for (String columnName : columnNames) {
                dataFrame.addColumn(columnName);
            }
            while (file.hasNext()) {
                String[] columnValues = file.nextLine().split(",");
                if (columnValues.length != columnNames.length) {
                    throw new DataLoadException("Wrong number of values for number of rows", fileName);
                }
                for (int i = 0; i < columnValues.length; i++) {
                    dataFrame.addValue(columnNames[i], columnValues[i]);
                }
            }
        } catch (IOException e) {
            throw new DataLoadException("Failed to read file: ", fileName);
        }
    }
}

