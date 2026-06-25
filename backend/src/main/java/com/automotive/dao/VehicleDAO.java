package com.automotive.dao;

import com.automotive.db.DBConnection;
import com.automotive.model.Vehicle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * VehicleDAO — JDBC data-access object for the {@code VEHICLE} table.
 *
 * <p><b>Migration notes (Java 8 → Java 21):</b>
 * <ol>
 *   <li><b>Resource leaks fixed:</b> every JDBC resource (Connection,
 *       PreparedStatement, ResultSet) is now opened inside a
 *       try-with-resources block, guaranteeing closure even when an
 *       exception is thrown. The legacy addVehicle and getAllVehicles
 *       methods never closed resources.</li>
 *   <li><b>Modern stream collector:</b> result-set rows are accumulated
 *       into an ArrayList and then converted with stream().toList()
 *       (Java 16+), returning an unmodifiable view.</li>
 *   <li><b>Optional return type:</b> getVehicleById returns
 *       Optional&lt;Vehicle&gt; instead of null, making the absence of a
 *       result explicit at the type level.</li>
 *   <li><b>Named SQL constants:</b> raw SQL strings are extracted to named
 *       constants, improving readability and testability.</li>
 *   <li><b>Immutable record:</b> row mapping produces Vehicle records
 *       (immutable) instead of mutable POJOs.</li>
 * </ol>
 *
 * <p>Database schema (unchanged):
 * VEHICLE(VEHICLE_ID NUMBER PK, OWNER_NAME VARCHAR2(100),
 * MODEL_NAME VARCHAR2(100), REG_NUMBER VARCHAR2(20))
 */
public class VehicleDAO {

    private static final Logger LOG = Logger.getLogger(VehicleDAO.class.getName());

    // -----------------------------------------------------------------------
    // SQL constants
    // -----------------------------------------------------------------------
    private static final String SQL_INSERT =
            "INSERT INTO VEHICLE (VEHICLE_ID, OWNER_NAME, MODEL_NAME, REG_NUMBER) VALUES (?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL =
            "SELECT VEHICLE_ID, OWNER_NAME, MODEL_NAME, REG_NUMBER FROM VEHICLE ORDER BY VEHICLE_ID";
    private static final String SQL_SELECT_BY_ID =
            "SELECT VEHICLE_ID, OWNER_NAME, MODEL_NAME, REG_NUMBER FROM VEHICLE WHERE VEHICLE_ID = ?";
    private static final String SQL_UPDATE =
            "UPDATE VEHICLE SET OWNER_NAME = ?, MODEL_NAME = ?, REG_NUMBER = ? WHERE VEHICLE_ID = ?";
    private static final String SQL_DELETE =
            "DELETE FROM VEHICLE WHERE VEHICLE_ID = ?";

    // -----------------------------------------------------------------------
    // CRUD operations
    // -----------------------------------------------------------------------

    /**
     * Inserts a new vehicle record.
     *
     * <p><b>Legacy issue fixed:</b> the original method did not close the
     * Connection or PreparedStatement; both are now managed by
     * try-with-resources.
     *
     * @param vehicle the vehicle to insert
     * @return true if exactly one row was inserted
     * @throws SQLException on JDBC error
     */
    public boolean addVehicle(Vehicle vehicle) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT)) {

            ps.setInt(1, vehicle.vehicleId());
            ps.setString(2, vehicle.ownerName());
            ps.setString(3, vehicle.modelName());
            ps.setString(4, vehicle.regNumber());

            int rows = ps.executeUpdate();
            LOG.fine(() -> "addVehicle: inserted %d row(s) for vehicleId=%d"
                    .formatted(rows, vehicle.vehicleId()));
            return rows == 1;
        }
    }

    /**
     * Retrieves all vehicle records ordered by VEHICLE_ID.
     *
     * <p><b>Legacy issue fixed:</b> the original method did not close the
     * Connection, PreparedStatement, or ResultSet.
     *
     * @return unmodifiable list of all vehicles (empty list if none exist)
     * @throws SQLException on JDBC error
     */
    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> accumulator = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                accumulator.add(mapRow(rs));
            }
        }

        // Java 16+ .toList() returns an unmodifiable list
        return accumulator.stream().toList();
    }

    /**
     * Retrieves a single vehicle by its primary key.
     *
     * <p><b>Legacy improvement:</b> returns Optional instead of null to
     * make absent-record handling explicit.
     *
     * @param vehicleId the primary key to look up
     * @return an Optional containing the vehicle, or empty if not found
     * @throws SQLException on JDBC error
     */
    public Optional<Vehicle> getVehicleById(int vehicleId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, vehicleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Updates the mutable fields of an existing vehicle record.
     * VEHICLE_ID (primary key) is never modified.
     *
     * @param vehicle vehicle carrying the updated field values
     * @return true if exactly one row was updated
     * @throws SQLException on JDBC error
     */
    public boolean updateVehicle(Vehicle vehicle) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, vehicle.ownerName());
            ps.setString(2, vehicle.modelName());
            ps.setString(3, vehicle.regNumber());
            ps.setInt(4, vehicle.vehicleId());

            int rows = ps.executeUpdate();
            LOG.fine(() -> "updateVehicle: updated %d row(s) for vehicleId=%d"
                    .formatted(rows, vehicle.vehicleId()));
            return rows == 1;
        }
    }

    /**
     * Deletes a vehicle record by its primary key.
     *
     * @param vehicleId the primary key of the record to delete
     * @return true if exactly one row was deleted
     * @throws SQLException on JDBC error
     */
    public boolean deleteVehicle(int vehicleId) throws SQLException {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, vehicleId);

            int rows = ps.executeUpdate();
            LOG.fine(() -> "deleteVehicle: deleted %d row(s) for vehicleId=%d"
                    .formatted(rows, vehicleId));
            return rows == 1;
        }
    }

    // -----------------------------------------------------------------------
    // Row mapping
    // -----------------------------------------------------------------------

    /**
     * Maps the current row of a ResultSet to a Vehicle record.
     *
     * @param rs an open ResultSet positioned on a valid row
     * @return an immutable Vehicle record
     * @throws SQLException if any column access fails
     */
    private Vehicle mapRow(ResultSet rs) throws SQLException {
        return new Vehicle(
                rs.getInt("VEHICLE_ID"),
                rs.getString("OWNER_NAME"),
                rs.getString("MODEL_NAME"),
                rs.getString("REG_NUMBER")
        );
    }
}