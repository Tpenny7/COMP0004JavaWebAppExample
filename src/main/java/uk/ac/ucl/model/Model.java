package uk.ac.ucl.model;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.ucl.model.io.DataLoadException;
import uk.ac.ucl.model.io.DataLoader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class Model
{
  DataFrame patients;
  DataLoader fileLoader = new DataLoader();
  Map<String, String> patientSummary;

  public void readFile(String fileName)
  {
      fileLoader.importData(fileName);
      patients = fileLoader.getDataFrame();
  }

  // This also returns dummy data. The real version should use the keyword parameter to search
  // the data and return a list of matching items.
  public List<String> searchFor(String keyword)
  {
    return List.of("Search keyword is: "+ keyword, "result1", "result2", "result3");
  }

  public void buildPatientSummary(){
    patientSummary = new HashMap<>();
    Column firstNames = patients.getColumn("FIRST");
    Column lastNames = patients.getColumn("LAST");
    Column IDs = patients.getColumn("ID");
    int rowCount = patients.getRowCount();
    for (int i = 0; i<rowCount; i++){
      patientSummary.put(IDs.getRowValue(i),
              firstNames.getRowValue(i).split("[0-9]",2)[0] + " "
                      + lastNames.getRowValue(i).split("[0-9]",2)[0]);
    }
  }

  public Map<String,String> getPatientSummary(){return patientSummary;}

  public List<String> getPatientAttribute(String columnName){
    ArrayList<String> patientAttribute = new ArrayList<>();
    try{
    Column attribute = patients.getColumn(columnName);
    int rowCount = patients.getRowCount();
    for (int i = 0; i<rowCount; i++){
      patientAttribute.add(attribute.getRowValue(i));
    }
    return patientAttribute;
    }catch (ColumnNotFoundException e){
      throw new DataLoadException("Column not found", e);
    }
  }
}
