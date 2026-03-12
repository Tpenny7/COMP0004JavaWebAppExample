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

@WebServlet("/attribute")
public class AttributeNumbers extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String attribute = request.getParameter("name");
        Model model = ModelFactory.getModel();
        if (attribute == null || attribute.isBlank()){
            request.setAttribute("errorMessage", "Missing patient id");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
        try{
            Map<String, ArrayList<String[]>> attributeNumbers = model.attributeNumbers(attribute);
            request.setAttribute("attributeNumbers", attributeNumbers);
            request.getRequestDispatcher("/attributeNumbers.jsp").forward(request,response);

        } catch (DataLoadException e) {
            request.setAttribute("errorMessage", "Error loading patient: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
