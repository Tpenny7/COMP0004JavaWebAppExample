package uk.ac.ucl.model;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import uk.ac.ucl.model.io.DataLoadException;
import uk.ac.ucl.model.io.DataLoader;
import uk.ac.ucl.model.io.DataWriteException;

import java.io.IOException;
import java.nio.file.Path;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Model {
    private DataFrame patients;
    private final DataLoader fileLoader = new DataLoader();
    private Map<String, String> patientSummary;
    private Map<String, Integer> patientIDtoRow;
    private ArrayList<String> columnNames;

    public void readFile(Path file) {
        fileLoader.importData(file);
        patients = fileLoader.getDataFrame();
        columnNames = patients.getColumnNames();
    }

    public void savePatientsToCsv(Path tmp, Path destination) {
        int rows = patients.getRowCount();
        //write to a temporary file
        try (java.io.Writer writer = new java.io.FileWriter(tmp.toFile());
             CSVPrinter printer = new CSVPrinter(writer,
                     CSVFormat.DEFAULT.withHeader(columnNames.toArray(new String[0])))) {

            for (int r = 0; r < rows; r++) {
                List<String> record = new ArrayList<>();
                for (String col : columnNames) {
                    String v = patients.getValue(col, r);
                    record.add(v == null ? "" : v);
                }
                printer.printRecord(record);
            }
            //write to the final file if no errors occur
            java.nio.file.Files.move(tmp, destination,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                    java.nio.file.StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new DataWriteException("Error saving CSV, " + e.getMessage());
        }
    }

    public void deletePatient(String id) {
        try {
            Integer row = patientIDtoRow.get(id);
            if (row == null) {
                throw new DataLoadException("Patient with id " + id + " not found");
            }
            for (Column c : patients) {
                c.deleteRow(row);
            }
            patientSummary.remove(id);
            patientIDtoRow.remove(id);
            //update the id to row map to shift the subsequent rows up
            for (Map.Entry<String, Integer> entry : patientIDtoRow.entrySet()) {
              int currRow = entry.getValue();
                if (currRow > row) {
                    entry.setValue(currRow - 1);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            throw new DataLoadException("Error loading data, " + e.getMessage());
        }
    }

    public void editPatient(Map<String, String> values, String id) {
        Integer row = patientIDtoRow.get(id);
        if (row == null) {
            throw new DataLoadException("Patient with id " + id + " not found");
        }
        try {
            for (Map.Entry<String, String> entry : values.entrySet()) {
                String colName = entry.getKey();
                Column c = patients.getColumn(colName);
                c.setRowValue(row, entry.getValue());
            }
            updatePatientSummary(values, id);
        } catch (ColumnNotFoundException e) {
            throw new DataWriteException("Error writing data" + e.getMessage());
        }
    }

    public void updatePatientSummary(Map<String, String> patient, String id) {
        try {
          //updates the first name in the patientsummary map
            if (patient.containsKey("FIRST")) {
                String last = patientSummary.get(id).split(" ")[1];
                patientSummary.put(id, patient.get("FIRST").split("[0-9]")[0] + " " + last);
            }
          //updates the last name in the patientsummary map
            if (patient.containsKey("LAST")) {
                String first = patientSummary.get(id).split(" ")[0];
                patientSummary.put(id, first + " " + patient.get("LAST").split("[0-9]")[0]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataLoadException("Patient summary has invalid format. ID = " + id, e);
        } catch (NullPointerException e) {
            throw new DataLoadException("Patient not found in patient summary. ID = " + id + " ." +  e.getMessage());
        }
    }

    public void addPatient(Map<String, String> values) {
        if (values == null) throw new InvalidParameterException("values must not be null");
        String patientID = values.get("ID");
        String first = values.get("FIRST");
        String last = values.get("LAST");
        if (patientID == null || patientID.isBlank()) {
            throw new InvalidParameterException("Missing ID");
        }
        if (first == null || last == null || first.isBlank() || last.isBlank()) {
            throw new InvalidParameterException("Missing Name");
        }
        try {
            patientSummary.put(patientID, first + " " + last);
            for (String columnName : columnNames) {
                Column attribute = patients.getColumn(columnName);
                attribute.addRowValue(values.getOrDefault(columnName, ""));
            }
            patientIDtoRow.put(patientID, patients.getRowCount() - 1);
        } catch (ColumnNotFoundException | NullPointerException e) {
            throw new DataWriteException("Error loading data, " + e.getMessage());
        }
    }

    public String generateNewId() {
        String id = java.util.UUID.randomUUID().toString();
        while (patientIDtoRow.containsKey(id)) {
            id = java.util.UUID.randomUUID().toString();
        }
        return id;
    }


    //returns the number of patients who share a particular attribute
    public Map<String, ArrayList<String[]>> attributeNumbers(String attributeName) {
        Map<String, ArrayList<String[]>> results = new LinkedHashMap<>();
        int rowCount = patients.getRowCount();
        try {
            Column attribute = patients.getColumn(attributeName);
            Column idCol = patients.getColumn("ID");
            for (int i = 0; i < rowCount; i++) {
                String key = attribute.getRowValue(i);
                String id = idCol.getRowValue(i);
                results.computeIfAbsent(key, k -> new ArrayList<>())
                        .add(new String[]{id, patientSummary.get(id)});
            }
          //sorts the map from most people sharing to least
            results = results.entrySet()
                    .stream()
                    .sorted(Comparator.comparingInt(
                                    (Map.Entry<String, ArrayList<String[]>> e) -> e.getValue().size())
                            .reversed())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (a, b) -> a,
                            LinkedHashMap::new
                    ));
            return results;
        } catch (ColumnNotFoundException e) {
            throw new DataLoadException("Column not found", e);
        }
    }

    //returns a map of people who live in the same place (ADDRESS, ZIP, STATE and CITY all shared)
    public Map<String, ArrayList<String[]>> sameLocations() {
        Map<String, ArrayList<String[]>> results = new HashMap<>();
        int rowCount = patients.getRowCount();
        try {
            Column addressCol = patients.getColumn("ADDRESS");
            Column cityCol = patients.getColumn("CITY");
            Column stateCol = patients.getColumn("STATE");
            Column zipCol = patients.getColumn("ZIP");
            Column idCol = patients.getColumn("ID");
            for (int i = 0; i < rowCount; i++) {
                String addr = addressCol.getRowValue(i);
                String city = cityCol.getRowValue(i);
                String state = stateCol.getRowValue(i);
                String zip = zipCol.getRowValue(i);

                String key = addr + " | " + city + " | " + state + " | " + zip;
                results.putIfAbsent(key, new ArrayList<>());
                ArrayList<String[]> list = results.get(key);
                String id = idCol.getRowValue(i);
                list.add(new String[]{id, patientSummary.get(id)});
            }
            return results;
        } catch (ColumnNotFoundException e) {
            throw new DataLoadException("Column not found", e);
        }
    }

    public Map<String, ArrayList<String>> searchFor(String keyword) {
        Map<String, ArrayList<String>> results = new HashMap<>();
        if (keyword == null || keyword.isBlank()) {
            throw new DataLoadException("No keyword provided");
        }
        String k = keyword.toLowerCase();
        try {
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
        } catch (ColumnNotFoundException | IndexOutOfBoundsException e) {
            throw new DataLoadException("Error loading data, " + e.getMessage());
        }
    }

    public void buildPatientSummary() {
        patientSummary = new HashMap<>();
        patientIDtoRow = new HashMap<>();
        try {
            Column firstNames = patients.getColumn("FIRST");
            Column lastNames = patients.getColumn("LAST");
            Column IDs = patients.getColumn("ID");
            int rowCount = patients.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                patientSummary.put(IDs.getRowValue(i),
                        firstNames.getRowValue(i).split("[0-9]", 2)[0] + " "
                                + lastNames.getRowValue(i).split("[0-9]", 2)[0]);
                patientIDtoRow.put(IDs.getRowValue(i), i);
            }
        } catch (ColumnNotFoundException e) {
            throw new DataLoadException("Column not found" + e.getMessage());
        }
    }

    public Map<String, String> getPatientSummary() {
        return patientSummary;
    }

    public ArrayList<String> getAttributeNames() {
        return patients.getColumnNames();
    }

    public Map<String, String> getSpecificPatient(String ID) {
        Integer row = patientIDtoRow.get(ID);
        if (row == null) {
            throw new DataLoadException("Patient with id " + ID + " not found");
        }
        try {
            Map<String, String> patient = new LinkedHashMap<>();
            for (String columnName : columnNames) {
                patient.put(columnName, patients.getValue(columnName, row));
            }
            return patient;
        } catch (ColumnNotFoundException e) {
            throw new DataLoadException("Column not found" + e.getMessage());
        }
    }

    public String getOldestPerson() {
        try {
            LocalDate oldest = LocalDate.MAX;
            int oldestPatientRow = 0;
            Column dobCol = patients.getColumn("BIRTHDATE");
            Column deathCol = patients.getColumn("DEATHDATE");
            int rowCount = patients.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                if (deathCol.getRowValue(i) == null || deathCol.getRowValue(i).isEmpty()) {
                    LocalDate curr = LocalDate.parse(dobCol.getRowValue(i));
                    if (curr.isBefore(oldest)) {
                        oldest = curr;
                        oldestPatientRow = i;
                    }
                }
            }
            return patients.getColumn("ID").getRowValue(oldestPatientRow);
        } catch (ColumnNotFoundException e) {
            throw new DataLoadException("Column not found", e);
        } catch (DateTimeParseException e) {
            throw new DataLoadException("Invalid date format, " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataLoadException("Error loading data, " + e.getMessage());
        }
    }

    public String getYoungestPerson() {
        try {
            LocalDate youngest = LocalDate.MIN;
            int youngestPatientRow = 0;
            Column dobCol = patients.getColumn("BIRTHDATE");
            Column deathCol = patients.getColumn("DEATHDATE");
            int rowCount = patients.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                if (deathCol.getRowValue(i) == null || deathCol.getRowValue(i).isEmpty()) {
                    LocalDate curr = LocalDate.parse(dobCol.getRowValue(i));
                    if (curr.isAfter(youngest)) {
                        youngest = curr;
                        youngestPatientRow = i;
                    }
                }
            }
            return patients.getColumn("ID").getRowValue(youngestPatientRow);
        } catch (ColumnNotFoundException e) {
            throw new DataLoadException("Column not found", e);
        } catch (DateTimeParseException e) {
            throw new DataLoadException("Invalid date format, " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new DataLoadException("Error loading data, " + e.getMessage());
        }
    }
}
