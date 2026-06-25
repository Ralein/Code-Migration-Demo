package com.automotive;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/editVehicle")
public class EditVehicleServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        int id =
                Integer.parseInt(
                        request.getParameter("id"));

        VehicleDAO dao =
                new VehicleDAO();

        Vehicle vehicle =
                dao.getVehicleById(id);

        request.setAttribute(
                "vehicle",
                vehicle);

        request.getRequestDispatcher(
                "editVehicle.jsp")
                .forward(request, response);
    }
}