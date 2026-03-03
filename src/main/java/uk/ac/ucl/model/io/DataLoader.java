package uk.ac.ucl.model.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import uk.ac.ucl.model.DataFrame;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class DataLoader {
    DataFrame dataFrame;

    public DataFrame getDataFrame() {
        return dataFrame;
    }

    public void importData(String fileName) {
        dataFrame = new DataFrame();
        CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        try (Reader reader = Files.newBufferedReader(Path.of(fileName));
             CSVParser parser = new CSVParser(reader, format)) {
            List<String> headers = parser.getHeaderNames();
            for (String columnName : headers) {
                dataFrame.addColumn(columnName);
            }
            for (CSVRecord r : parser) {
                for (String h : headers) {
                    dataFrame.addValue(h, r.get(h));
                }
            }
        } catch (IOException e) {
            throw new DataLoadException("Failed to read file: " + fileName, e);
        }
    }
}

