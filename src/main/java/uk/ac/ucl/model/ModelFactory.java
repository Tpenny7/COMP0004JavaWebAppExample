package uk.ac.ucl.model;

import java.io.IOException;

import static uk.ac.ucl.model.AppConfig.PATIENT_CSV_PATH;

public class ModelFactory {
    private static Model model;

    public static Model getModel() throws IOException {
        if (model == null) {
            model = new Model();
            model.readFile(PATIENT_CSV_PATH);
            model.buildPatientSummary();
        }
        return model;
    }
}
