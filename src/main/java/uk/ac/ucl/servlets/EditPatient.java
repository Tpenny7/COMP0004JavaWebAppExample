package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.io.DataLoadException;
import uk.ac.ucl.model.io.DataWriteException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

import static uk.ac.ucl.model.AppConfig.PATIENT_CSV_PATH;


@WebServlet("/editPatient")
public class EditPatient extends HttpServlet {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("uuuu-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Model model = ModelFactory.getModel();
            String id = request.getParameter("id");
            if (id == null || id.isBlank()) {
                request.setAttribute("errorMessage", "Missing patient id");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            Map<String, String> patient = model.getSpecificPatient(id);
            request.setAttribute("id", id);
            request.setAttribute("patient", patient);
            request.getRequestDispatcher("/editPatient.jsp").forward(request, response);
        } catch (DataLoadException e) {
            request.setAttribute("errorMessage", "Error adding patient: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Model model = ModelFactory.getModel();

            Map<String, String> values = new HashMap<>();
            String id = request.getParameter("id");
            if (id == null || id.isBlank()) {
                request.setAttribute("errorMessage", "Missing patient id");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            String birthdate = request.getParameter("BIRTHDATE");
            String deathdate = request.getParameter("DEATHDATE");
            values.put("BIRTHDATE", birthdate);
            values.put("DEATHDATE", deathdate);
            values.put("SSN", request.getParameter("SSN"));
            values.put("DRIVERS", request.getParameter("DRIVERS"));
            values.put("PASSPORT", request.getParameter("PASSPORT"));
            values.put("PREFIX", request.getParameter("PREFIX"));
            values.put("FIRST", request.getParameter("FIRST"));
            values.put("LAST", request.getParameter("LAST"));
            values.put("SUFFIX", request.getParameter("SUFFIX"));
            values.put("MAIDEN", request.getParameter("MAIDEN"));
            values.put("MARITAL", request.getParameter("MARITAL"));
            values.put("RACE", request.getParameter("RACE"));
            values.put("ETHNICITY", request.getParameter("ETHNICITY"));
            values.put("GENDER", request.getParameter("GENDER"));
            values.put("BIRTHPLACE", request.getParameter("BIRTHPLACE"));
            values.put("ADDRESS", request.getParameter("ADDRESS"));
            values.put("CITY", request.getParameter("CITY"));
            values.put("STATE", request.getParameter("STATE"));
            values.put("ZIP", request.getParameter("ZIP"));

            if (values.get("FIRST") == null || values.get("FIRST").isBlank()
                    || values.get("LAST") == null || values.get("LAST").isBlank()) {

                request.setAttribute("errorMessage", "First and last name are required.");
                request.setAttribute("patient", values);
                request.setAttribute("id", id);
                request.getRequestDispatcher("/editPatient.jsp")
                        .forward(request, response);
                return;
            }
            if (birthdate == null || birthdate.isEmpty() || !isValidDate(birthdate)) {
                request.setAttribute("errorMessage", "Birthdate must be in YYYY-MM-DD format.");
                request.setAttribute("patient", values);
                request.setAttribute("id", id);
                request.getRequestDispatcher("/editPatient.jsp").forward(request, response);
                return;
            }
            if (!isValidDate(deathdate)) {
                request.setAttribute("errorMessage", "Deathdate must be in YYYY-MM-DD format.");
                request.setAttribute("patient", values);
                request.setAttribute("id", id);
                request.getRequestDispatcher("/editPatient.jsp").forward(request, response);
                return;
            }


            model.editPatient(values, id);

            Path tmp = Paths.get("data/patients.tmp");
            model.savePatientsToCsv(tmp, PATIENT_CSV_PATH);

            //shows patient summary of new patient
            response.sendRedirect(request.getContextPath() + "/patient?id=" + id);


        } catch (IOException e) {
            request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (DataWriteException | DataLoadException e) {
            request.setAttribute("errorMessage", "Error editing patient: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    public boolean isValidDate(String s) {
        if (s == null || s.isBlank()) return true;
        try {
            LocalDate.parse(s, DATE_FMT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
