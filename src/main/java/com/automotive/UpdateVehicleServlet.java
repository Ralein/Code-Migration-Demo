package com.automotive;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/updateVehicle")
public class UpdateVehicleServlet extends HttpServlet {

    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException,
            IOException {

        Vehicle vehicle =
                new Vehicle();

        vehicle.setVehicleId(
                Integer.parseInt(
                request.getParameter(
                "vehicleId")));

        vehicle.setOwnerName(
                request.getParameter(
                "ownerName"));

        vehicle.setModelName(
                request.getParameter(
                "modelName"));

        vehicle.setRegNumber(
                request.getParameter(
                "regNumber"));

        VehicleDAO dao =
                new VehicleDAO();

        dao.updateVehicle(vehicle);

        response.sendRedirect(
                "viewVehicles");
    }
}