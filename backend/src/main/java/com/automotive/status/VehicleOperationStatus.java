package com.automotive.status;

/**
 * VehicleOperationStatus — sealed interface modelling all possible outcomes
 * of a vehicle CRUD operation.
 *
 * <p><b>Migration note (Java 8 → Java 21):</b>
 * The legacy servlets signalled outcomes via HTTP redirects and raw HTML
 * strings (e.g. {@code response.getWriter().println("<h2>error</h2>")}).
 * This sealed hierarchy replaces that ad-hoc approach with a type-safe,
 * exhaustively matchable result type consumed by the servlet layer.
 *
 * <p>Permitted subtypes:
 * <ul>
 *   <li>{@link Success} — operation completed; carries an optional payload.</li>
 *   <li>{@link NotFound} — requested vehicle ID does not exist.</li>
 *   <li>{@link ValidationFailure} — input did not pass validation rules.</li>
 *   <li>{@link DatabaseFailure} — a JDBC-level error occurred.</li>
 * </ul>
 *
 * <p>Usage with Java 21 pattern-matching switch:
 * <pre>{@code
 * String message = switch (status) {
 *     case Success<?> s        -> "OK";
 *     case NotFound   nf       -> "Vehicle not found: " + nf.vehicleId();
 *     case ValidationFailure v -> "Validation error: " + v.reason();
 *     case DatabaseFailure   d -> "DB error: " + d.cause().getMessage();
 * };
 * }</pre>
 */
public sealed interface VehicleOperationStatus
        permits VehicleOperationStatus.Success,
                VehicleOperationStatus.NotFound,
                VehicleOperationStatus.ValidationFailure,
                VehicleOperationStatus.DatabaseFailure {

    /**
     * Operation succeeded. {@code payload} carries the result (e.g. a
     * {@link com.automotive.model.Vehicle} or a {@code List<Vehicle>}),
     * or {@code null} for void operations such as delete.
     */
    record Success<T>(T payload) implements VehicleOperationStatus {}

    /**
     * The requested vehicle ID was not found in the database.
     */
    record NotFound(int vehicleId) implements VehicleOperationStatus {}

    /**
     * Input validation failed before any database interaction.
     *
     * @param reason human-readable description of the validation rule violated
     */
    record ValidationFailure(String reason) implements VehicleOperationStatus {}

    /**
     * A JDBC exception prevented the operation from completing.
     *
     * @param cause the underlying {@link Exception}
     */
    record DatabaseFailure(Exception cause) implements VehicleOperationStatus {}
}