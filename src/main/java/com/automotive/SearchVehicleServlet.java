package com.automotive;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/searchVehicle")
public class SearchVehicleServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        int vehicleId =
                Integer.parseInt(
                        request.getParameter("vehicleId"));

        VehicleDAO dao =
                new VehicleDAO();

        Vehicle vehicle =
                dao.searchVehicle(vehicleId);

        request.setAttribute(
                "vehicle",
                vehicle);

        request.getRequestDispatcher(
                "searchResult.jsp")
                .forward(request, response);
    }
}