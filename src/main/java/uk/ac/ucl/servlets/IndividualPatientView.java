package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.io.DataLoadException;

import java.io.IOException;
import java.util.Map;

@WebServlet ("/patient")
public class IndividualPatientView extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            request.setAttribute("errorMessage", "Missing patient id");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }

        try {
            Model model = ModelFactory.getModel();
            Map<String, String> patient = model.getSpecificPatient(id);

            if (patient == null) {
                request.setAttribute("errorMessage", "No patient found for id: " + id);
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            request.setAttribute("patient", patient);
            request.setAttribute("id", id);
            request.getRequestDispatcher("/patient.jsp").forward(request, response);
        } catch (DataLoadException e){
            request.setAttribute("errorMessage", "Error loading patient: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}