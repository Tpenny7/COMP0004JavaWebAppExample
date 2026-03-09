package uk.ac.ucl.model;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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
  Map<String, Integer> patientIDtoColumn;
  ArrayList<String> columnNames;

  public void readFile(String fileName)
  {
      fileLoader.importData(fileName);
      patients = fileLoader.getDataFrame();
      columnNames = patients.getColumnNames();
  }

  // This also returns dummy data. The real version should use the keyword parameter to search
  // the data and return a list of matching items.
  public Map<String, ArrayList<String>> searchFor(String keyword) {
    Map<String, ArrayList<String>> results = new HashMap<>();

    String k = keyword.toLowerCase();
    Column idCol = patients.getColumn("ID");
    int rowCount = patients.getRowCount();

    for (Column c : patients) {
      for (int i = 0; i < rowCount; i++) {
        String cell = c.getRowValue(i);
        if (cell == null) continue;

        if (cell.toLowerCase().contains(k)) {
          String patientID = idCol.getRowValue(i);
          results.putIfAbsent(patientID, new ArrayList<>());
          ArrayList<String> list = results.get(patientID);

          if (list.isEmpty()) {
            list.add(patientSummary.get(patientID));
          }
          list.add(c.getName() + ": " + cell);
        }
      }
    }
    return results;
  }


  public void buildPatientSummary(){
    patientSummary = new HashMap<>();
    patientIDtoColumn = new HashMap<>();
    Column firstNames = patients.getColumn("FIRST");
    Column lastNames = patients.getColumn("LAST");
    Column IDs = patients.getColumn("ID");
    int rowCount = patients.getRowCount();
    for (int i = 0; i<rowCount; i++){
      patientSummary.put(IDs.getRowValue(i),
              firstNames.getRowValue(i).split("[0-9]",2)[0] + " "
                      + lastNames.getRowValue(i).split("[0-9]",2)[0]);
      patientIDtoColumn.put(IDs.getRowValue(i), i);
    }
  }

  public Map<String,String> getPatientSummary(){return patientSummary;}

  public Map<String,String> getSpecificPatient(String ID){
    try {
      int column = patientIDtoColumn.get(ID);
      Map<String, String> patient = new LinkedHashMap<>();
      for (String columnName : columnNames) {
        patient.put(columnName, patients.getValue(columnName, column));
      }
      return patient;
    } catch (IllegalArgumentException e){
        throw new DataLoadException("Patient with ID" + ID + "not found", e);
    } catch (ColumnNotFoundException e){
        throw new DataLoadException("Column not found", e);
    }
  }

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
