package com.automotive;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/deleteVehicle")
public class DeleteVehicleServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        int vehicleId =
                Integer.parseInt(
                        request.getParameter("id"));

        VehicleDAO dao =
                new VehicleDAO();

        boolean result =
                dao.deleteVehicle(vehicleId);

        if(result) {

            response.sendRedirect(
                    "viewVehicles?message=deleted");

        } else {

            response.sendRedirect(
                    "viewVehicles?message=error");
        }
    }
}