package uk.ac.ucl.model;

import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
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
  Map<String, Integer> patientIDtoRow;
  ArrayList<String> columnNames;

  public void readFile(String fileName)
  {
      fileLoader.importData(fileName);
      patients = fileLoader.getDataFrame();
      columnNames = patients.getColumnNames();
  }

  public Map<String, ArrayList<String[]>> attributeNumbers(String attributeName){

  }

  public Map<String, ArrayList<String[]>> sameLocations(){
    Map<String, ArrayList<String[]>> results = new HashMap<>();
    int rowCount = patients.getRowCount();
    //ADDRESS,CITY,STATE,ZIP
    try {
      Column addressCol = patients.getColumn("ADDRESS");
      Column cityCol = patients.getColumn("CITY");
      Column stateCol = patients.getColumn("STATE");
      Column zipCol = patients.getColumn("ZIP");
      Column idCol = patients.getColumn("ID");
      for (int i = 0; i < rowCount; i++) {
        String addr  = addressCol.getRowValue(i);
        String city  = cityCol.getRowValue(i);
        String state = stateCol.getRowValue(i);
        String zip   = zipCol.getRowValue(i);

        String key = addr + " | " + city + " | " + state + " | " + zip;;
        results.putIfAbsent(key, new ArrayList<>());
        ArrayList<String[]> list = results.get(key);
        String id = idCol.getRowValue(i);
        list.add(new String[]{id, patientSummary.get(id)});
      }
      return results;
    }catch (ColumnNotFoundException e){
      throw new DataLoadException("Column not found", e);
    }
  }

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
    patientIDtoRow = new HashMap<>();
    Column firstNames = patients.getColumn("FIRST");
    Column lastNames = patients.getColumn("LAST");
    Column IDs = patients.getColumn("ID");
    int rowCount = patients.getRowCount();
    for (int i = 0; i<rowCount; i++){
      patientSummary.put(IDs.getRowValue(i),
              firstNames.getRowValue(i).split("[0-9]",2)[0] + " "
                      + lastNames.getRowValue(i).split("[0-9]",2)[0]);
      patientIDtoRow.put(IDs.getRowValue(i), i);
    }
  }


  public Map<String,String> getPatientSummary(){return patientSummary;}

  public ArrayList<String> getAttributeNames(){return patients.getColumnNames();}

  public Map<String,String> getSpecificPatient(String ID){
    try {
      int row = patientIDtoRow.get(ID);
      Map<String, String> patient = new LinkedHashMap<>();
      for (String columnName : columnNames) {
        patient.put(columnName, patients.getValue(columnName, row));
      }
      return patient;
    } catch (IllegalArgumentException e){
        throw new DataLoadException("Patient with ID" + ID + "not found", e);
    } catch (ColumnNotFoundException e){
        throw new DataLoadException("Column not found", e);
    }
  }

  public String getOldestPerson(){
    try {
      LocalDate oldest = LocalDate.MAX;
      int oldestPatientRow = 0;
      Column dobCol = patients.getColumn("BIRTHDATE");
      Column deathCol = patients.getColumn("DEATHDATE");
      int rowCount = patients.getRowCount();
      for(int i = 0; i<rowCount; i++){
        if (deathCol.getRowValue(i) == null || deathCol.getRowValue(i).isEmpty()){
          LocalDate curr = LocalDate.parse(dobCol.getRowValue(i));
          if(curr.isBefore(oldest)){
            oldest = curr;
            oldestPatientRow = i;
          }
        }
      }
      return patients.getColumn("ID").getRowValue(oldestPatientRow);
    }catch (ColumnNotFoundException e){
      throw new DataLoadException("Column not found", e);
    }
  }

  public String getYoungestPerson(){
    try {
      LocalDate youngest = LocalDate.MIN;
      int youngestPatientRow = 0;
      Column dobCol = patients.getColumn("BIRTHDATE");
      Column deathCol = patients.getColumn("DEATHDATE");
      int rowCount = patients.getRowCount();
      for(int i = 0; i<rowCount; i++){
        if (deathCol.getRowValue(i) == null || deathCol.getRowValue(i).isEmpty()){
          LocalDate curr = LocalDate.parse(dobCol.getRowValue(i));
          if(curr.isAfter(youngest)){
            youngest = curr;
            youngestPatientRow = i;
          }
        }
      }
      return patients.getColumn("ID").getRowValue(youngestPatientRow);
    }catch (ColumnNotFoundException e){
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
