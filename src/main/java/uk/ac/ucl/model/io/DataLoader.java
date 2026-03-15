package uk.ac.ucl.model.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import uk.ac.ucl.model.DataFrame;
import uk.ac.ucl.model.DuplicateColumnException;

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

    public void importData(Path file) {
        dataFrame = new DataFrame();
        CSVFormat format = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        try (Reader reader = Files.newBufferedReader(file);
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
            throw new DataLoadException("Failed to read file: " + file.toString(), e);
        } catch (IllegalArgumentException e) {
            throw new DataLoadException("File not found: " + file.toString(), e);
        } catch (DuplicateColumnException e) {
            throw new DataLoadException("Invalid file format: " + file.toString(), e);
        }
    }
}

