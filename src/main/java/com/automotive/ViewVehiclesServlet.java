package com.automotive;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ViewVehiclesServlet
 * Controller that handles requests to view the list of vehicles.
 */
@WebServlet("/viewVehicles")
public class ViewVehiclesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles HTTP GET requests.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Initialize the DAO to interact with the database (Model)
        VehicleDAO dao = new VehicleDAO();

        // 2. Retrieve the list of all vehicles
        List<Vehicle> vehicles = dao.getAllVehicles();

        // 3. Store the data in the request scope so the JSP can access it
        // Note: The key "vehicles" must exactly match the attribute name in your JSP
        request.setAttribute("vehicles", vehicles);

        // 4. Forward the request and response objects to your JSP file (View)
        // Adjust the filename if your JSP file is named differently (e.g., /view-vehicles.jsp)
        RequestDispatcher dispatcher = request.getRequestDispatcher("/viewVehicles.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handles HTTP POST requests by redirecting them to the GET handler.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}