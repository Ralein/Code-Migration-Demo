package com.automotive;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/addVehicle")
public class AddVehicleServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String vehicleIdStr = request.getParameter("vehicleId");
        String ownerName = request.getParameter("ownerName");
        String modelName = request.getParameter("modelName");
        String regNumber = request.getParameter("regNumber");

        // Empty field validation
        if(vehicleIdStr == null || vehicleIdStr.trim().isEmpty()
                || ownerName == null || ownerName.trim().isEmpty()
                || modelName == null || modelName.trim().isEmpty()
                || regNumber == null || regNumber.trim().isEmpty()) {

            response.getWriter().println(
                    "<h2>All fields are mandatory.</h2>");
            return;
        }

        int vehicleId;

        try {
            vehicleId = Integer.parseInt(vehicleIdStr);

            if(vehicleId <= 0) {
                response.getWriter().println(
                        "<h2>Vehicle ID must be greater than 0.</h2>");
                return;
            }

        } catch(NumberFormatException e) {

            response.getWriter().println(
                    "<h2>Vehicle ID must be numeric.</h2>");
            return;
        }

        // Owner name validation
        if(ownerName.length() < 3) {

            response.getWriter().println(
                    "<h2>Owner Name must contain at least 3 characters.</h2>");
            return;
        }

        // Registration number validation
        String regPattern = "[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}";

        if(!Pattern.matches(regPattern, regNumber)) {

            response.getWriter().println(
                    "<h2>Registration Number must be in format TS09AB1234.</h2>");
            return;
        }

        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleId(vehicleId);
        vehicle.setOwnerName(ownerName);
        vehicle.setModelName(modelName);
        vehicle.setRegNumber(regNumber);

        VehicleDAO dao = new VehicleDAO();

        boolean result = dao.addVehicle(vehicle);

        if(result) {

            response.sendRedirect(
                    "viewVehicles?message=success");

        } else {

            response.sendRedirect(
                    "addVehicle.jsp?message=error");
        }
    }
}