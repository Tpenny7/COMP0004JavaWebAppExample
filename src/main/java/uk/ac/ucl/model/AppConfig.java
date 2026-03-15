package uk.ac.ucl.model;

import java.nio.file.Path;

public final class AppConfig {
    private AppConfig() {}

    public static final Path PATIENT_CSV_PATH = Path.of("data", "patients10000.csv");
}

