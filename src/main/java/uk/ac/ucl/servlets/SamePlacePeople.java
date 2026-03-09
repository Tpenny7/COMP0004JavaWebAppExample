package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;
import uk.ac.ucl.model.io.DataLoadException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@WebServlet("/samePlace")
public class SamePlacePeople extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Model model = ModelFactory.getModel();
            Map<String, ArrayList<String[]>> addressPatients = model.sameLocations();
            if (addressPatients == null || addressPatients.isEmpty()) {
                request.setAttribute("errorMessage", "Error reading the data");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            request.setAttribute("addressPatients", addressPatients);
            request.getRequestDispatcher("/samePlace.jsp").forward(request, response);
        }catch (DataLoadException e){
            request.setAttribute("errorMessage", "Error loading patients: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
