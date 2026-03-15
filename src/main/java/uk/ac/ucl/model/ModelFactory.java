package uk.ac.ucl.model;

import java.io.IOException;
import java.nio.file.Paths;

public class ModelFactory {
    private static Model model;

    public static Model getModel() throws IOException {
        if (model == null) {
            model = new Model();
            model.readFile(Paths.get("data/patients100.csv"));
            model.buildPatientSummary();
        }
        return model;
    }
}
