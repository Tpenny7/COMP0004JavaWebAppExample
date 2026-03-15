package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.io.DataLoadException;

import java.io.IOException;
import java.util.Map;

@WebServlet("/patientList")
public class ViewPatientListServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Model model = ModelFactory.getModel();
            Map<String, String> patientSummary = model.getPatientSummary();
            request.setAttribute("patientSummary", patientSummary);

            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/patientList.jsp");
            dispatch.forward(request, response);
        } catch (IOException | DataLoadException e) {

            request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
            ServletContext context = getServletContext();
            RequestDispatcher dispatch = context.getRequestDispatcher("/error.jsp");
            dispatch.forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
