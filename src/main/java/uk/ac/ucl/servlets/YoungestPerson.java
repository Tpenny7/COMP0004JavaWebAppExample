package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.io.DataLoadException;

import java.io.IOException;

@WebServlet("/youngestPerson")
public class YoungestPerson extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{
            Model model = ModelFactory.getModel();
            String patientID = model.getYoungestPerson();
            String href = "/patient?id=" + patientID;
            RequestDispatcher rd = request.getRequestDispatcher(href);
            rd.forward(request,response);
        }catch (DataLoadException e){
            request.setAttribute("errorMessage", "Error loading patient: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
