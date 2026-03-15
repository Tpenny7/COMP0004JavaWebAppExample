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

@WebServlet("/deletePatient")
public class DeletePatient extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            request.setAttribute("errorMessage", "Missing patient id");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("id", id);
        request.getRequestDispatcher("/confirmDeletePatient.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        if (id == null || id.isBlank()) {
            request.setAttribute("errorMessage", "Missing patient id");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        try {
            Model model = ModelFactory.getModel();
            model.deletePatient(id);
            Path original = Paths.get("data/patients100.csv");
            Path tmp = Paths.get("data/patients100.tmp");
            model.savePatientsToCsv(tmp, original);

            request.setAttribute("message", "Patient " + id + " has been deleted.");
            request.getRequestDispatcher("/deletePatientSuccess.jsp")
                    .forward(request, response);

        } catch (DataLoadException e) {
            request.setAttribute("errorMessage", "Error deleting patient: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (DataWriteException e) {
            request.setAttribute("errorMessage", "Error adding patient: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}

