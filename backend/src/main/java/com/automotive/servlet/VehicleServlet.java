package com.automotive.servlet;

import com.automotive.dao.VehicleDAO;
import com.automotive.model.Vehicle;
import com.automotive.util.ApiResponse;
import com.automotive.util.ValidationUtil;
import com.automotive.util.ValidationUtil.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * VehicleServlet — unified REST endpoint serving JSON responses.
 *
 * <p><b>Migration notes (Java 8 to Java 21):</b>
 * <ul>
 *   <li>Namespace: javax.servlet.* replaced with jakarta.servlet.*
 *       (Jakarta EE 10 / Servlet 6.0).</li>
 *   <li>No JSP coupling: legacy servlets forwarded to editVehicle.jsp,
 *       searchResult.jsp, etc., and wrote raw HTML via
 *       response.getWriter().println(). This servlet returns only JSON —
 *       the Angular SPA handles all presentation.</li>
 *   <li>Centralised validation: request parsing and validation are
 *       delegated to ValidationUtil instead of being inlined.</li>
 *   <li>Pattern-matching switch: ValidationResult sealed type dispatch
 *       uses Java 21 exhaustive switch expressions.</li>
 *   <li>try-with-resources: all JDBC resources are managed by
 *       VehicleDAO which uses try-with-resources internally.</li>
 * </ul>
 *
 * <p>URL mapping: /api/vehicles/*
 *
 * <pre>
 *   GET    /api/vehicles          — list all vehicles
 *   GET    /api/vehicles/{id}     — get vehicle by id
 *   POST   /api/vehicles          — add vehicle (JSON body)
 *   PUT    /api/vehicles/{id}     — update vehicle (JSON body)
 *   DELETE /api/vehicles/{id}     — delete vehicle
 * </pre>
 */
@WebServlet("/api/vehicles/*")
public class VehicleServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(VehicleServlet.class.getName());
    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    private transient ObjectMapper objectMapper;
    private transient VehicleDAO vehicleDAO;

    @Override
    public void init() throws ServletException {
        this.vehicleDAO = new VehicleDAO();
        this.objectMapper = new ObjectMapper()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    // -----------------------------------------------------------------------
    // GET — list all or get by id
    // -----------------------------------------------------------------------

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            handleGetAll(res);
        } else {
            String idSegment = pathInfo.substring(1);
            handleGetById(idSegment, res);
        }
    }

    private void handleGetAll(HttpServletResponse res) throws IOException {
        try {
            List<Vehicle> vehicles = vehicleDAO.getAllVehicles();
            writeJson(res, HttpServletResponse.SC_OK,
                    ApiResponse.ok("Vehicles retrieved successfully", vehicles));
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getAllVehicles failed", e);
            writeJson(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ApiResponse.error("Database error: " + e.getMessage()));
        }
    }

    private void handleGetById(String idSegment, HttpServletResponse res) throws IOException {
        ValidationResult idVal = ValidationUtil.validateVehicleId(idSegment);

        switch (idVal) {
            case ValidationResult.Failure f ->
                writeJson(res, HttpServletResponse.SC_BAD_REQUEST,
                        ApiResponse.error(f.reason()));
            case ValidationResult.Success ignored -> {
                int vehicleId = Integer.parseInt(idSegment.strip());
                try {
                    Optional<Vehicle> found = vehicleDAO.getVehicleById(vehicleId);
                    if (found.isPresent()) {
                        writeJson(res, HttpServletResponse.SC_OK,
                                ApiResponse.ok("Vehicle found", found.get()));
                    } else {
                        writeJson(res, HttpServletResponse.SC_NOT_FOUND,
                                ApiResponse.error("Vehicle not found with id: " + vehicleId));
                    }
                } catch (SQLException e) {
                    LOG.log(Level.SEVERE, "getVehicleById failed for id=" + vehicleId, e);
                    writeJson(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ApiResponse.error("Database error: " + e.getMessage()));
                }
            }
        }
    }

    // -----------------------------------------------------------------------
    // POST — add vehicle
    // -----------------------------------------------------------------------

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        Vehicle vehicle = parseBody(req, res);
        if (vehicle == null) {
            return;
        }

        ValidationResult result = ValidationUtil.validateVehicle(vehicle);

        switch (result) {
            case ValidationResult.Failure f ->
                writeJson(res, HttpServletResponse.SC_BAD_REQUEST,
                        ApiResponse.error(f.reason()));
            case ValidationResult.Success ignored -> {
                try {
                    boolean added = vehicleDAO.addVehicle(vehicle);
                    if (added) {
                        writeJson(res, HttpServletResponse.SC_CREATED,
                                ApiResponse.ok("Vehicle added successfully", vehicle));
                    } else {
                        writeJson(res, HttpServletResponse.SC_CONFLICT,
                                ApiResponse.error("Vehicle could not be inserted (possible duplicate ID)."));
                    }
                } catch (SQLException e) {
                    LOG.log(Level.SEVERE, "addVehicle failed", e);
                    writeJson(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ApiResponse.error("Database error: " + e.getMessage()));
                }
            }
        }
    }

    // -----------------------------------------------------------------------
    // PUT — update vehicle
    // -----------------------------------------------------------------------

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            writeJson(res, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("Vehicle ID is required in the URL path for update."));
            return;
        }

        String idSegment = pathInfo.substring(1);
        ValidationResult idVal = ValidationUtil.validateVehicleId(idSegment);

        switch (idVal) {
            case ValidationResult.Failure f ->
                writeJson(res, HttpServletResponse.SC_BAD_REQUEST,
                        ApiResponse.error(f.reason()));
            case ValidationResult.Success ignored -> {
                int vehicleId = Integer.parseInt(idSegment.strip());

                Vehicle body = parseBody(req, res);
                if (body == null) {
                    return;
                }

                // Reconstruct vehicle using path id (authoritative) and body fields
                Vehicle vehicle = new Vehicle(
                        vehicleId,
                        body.ownerName(),
                        body.modelName(),
                        body.regNumber()
                );

                ValidationResult bodyVal = ValidationUtil.validateVehicle(vehicle);

                switch (bodyVal) {
                    case ValidationResult.Failure f ->
                        writeJson(res, HttpServletResponse.SC_BAD_REQUEST,
                                ApiResponse.error(f.reason()));
                    case ValidationResult.Success ok -> {
                        try {
                            boolean updated = vehicleDAO.updateVehicle(vehicle);
                            if (updated) {
                                writeJson(res, HttpServletResponse.SC_OK,
                                        ApiResponse.ok("Vehicle updated successfully", vehicle));
                            } else {
                                writeJson(res, HttpServletResponse.SC_NOT_FOUND,
                                        ApiResponse.error("Vehicle not found with id: " + vehicleId));
                            }
                        } catch (SQLException e) {
                            LOG.log(Level.SEVERE, "updateVehicle failed for id=" + vehicleId, e);
                            writeJson(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                    ApiResponse.error("Database error: " + e.getMessage()));
                        }
                    }
                }
            }
        }
    }

    // -----------------------------------------------------------------------
    // DELETE — delete vehicle
    // -----------------------------------------------------------------------

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            writeJson(res, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("Vehicle ID is required in the URL path for delete."));
            return;
        }

        String idSegment = pathInfo.substring(1);
        ValidationResult idVal = ValidationUtil.validateVehicleId(idSegment);

        switch (idVal) {
            case ValidationResult.Failure f ->
                writeJson(res, HttpServletResponse.SC_BAD_REQUEST,
                        ApiResponse.error(f.reason()));
            case ValidationResult.Success ignored -> {
                int vehicleId = Integer.parseInt(idSegment.strip());
                try {
                    boolean deleted = vehicleDAO.deleteVehicle(vehicleId);
                    if (deleted) {
                        writeJson(res, HttpServletResponse.SC_OK,
                                ApiResponse.ok("Vehicle deleted successfully",
                                        Map.of("vehicleId", vehicleId)));
                    } else {
                        writeJson(res, HttpServletResponse.SC_NOT_FOUND,
                                ApiResponse.error("Vehicle not found with id: " + vehicleId));
                    }
                } catch (SQLException e) {
                    LOG.log(Level.SEVERE, "deleteVehicle failed for id=" + vehicleId, e);
                    writeJson(res, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            ApiResponse.error("Database error: " + e.getMessage()));
                }
            }
        }
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    /**
     * Parses the request body as a Vehicle JSON object.
     * Returns null and writes a 400 response if parsing fails.
     */
    private Vehicle parseBody(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        try (InputStream is = req.getInputStream()) {
            return objectMapper.readValue(is, Vehicle.class);
        } catch (IOException e) {
            writeJson(res, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("Invalid JSON body: " + e.getMessage()));
            return null;
        }
    }

    /**
     * Serialises a response object to JSON and writes it to the HTTP response.
     */
    private void writeJson(HttpServletResponse res, int status, Object payload)
            throws IOException {
        res.setContentType(CONTENT_TYPE_JSON);
        res.setStatus(status);
        objectMapper.writeValue(res.getWriter(), payload);
    }
}