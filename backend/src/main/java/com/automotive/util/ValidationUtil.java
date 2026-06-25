package com.automotive.util;

import java.util.regex.Pattern;

/**
 * ValidationUtil — centralised, stateless validation helpers.
 *
 * <p><b>Migration note (Java 8 → Java 21):</b>
 * <ul>
 *   <li>Legacy: validation was inlined inside {@code AddVehicleServlet.doPost()}
 *       as a series of {@code if/else} checks mixing business logic with
 *       presentation concerns (e.g. {@code response.getWriter().println(...)}).</li>
 *   <li>Modern: all rules are extracted into a dedicated utility class.
 *       Java 21 pattern-matching {@code switch} expressions are used where the
 *       validation outcome drives branching behaviour.</li>
 * </ul>
 *
 * <p>Validation rules (from AGENTS.md / README.md):
 * <ul>
 *   <li>VEHICLE_ID : must be numeric and &gt; 0</li>
 *   <li>OWNER_NAME : minimum 3 characters</li>
 *   <li>REG_NUMBER : pattern {@code [A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}}</li>
 *   <li>All fields are mandatory (non-null, non-blank)</li>
 * </ul>
 */
public final class ValidationUtil {

    /** Registration number pattern — e.g. TS09AB1234 */
    private static final Pattern REG_NUMBER_PATTERN =
            Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$");

    private ValidationUtil() {
        // Utility class — no instances
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /**
     * Validates a raw {@code vehicleId} string parsed from a request parameter.
     *
     * @param raw the raw string value (may be {@code null} or empty)
     * @return a {@link ValidationResult} indicating success or the failure reason
     */
    public static ValidationResult validateVehicleId(String raw) {
        if (isBlank(raw)) {
            return ValidationResult.failure("vehicleId is required");
        }
        try {
            int id = Integer.parseInt(raw.strip());
            return id > 0
                    ? ValidationResult.success()
                    : ValidationResult.failure("vehicleId must be greater than 0");
        } catch (NumberFormatException e) {
            return ValidationResult.failure("vehicleId must be a numeric value");
        }
    }

    /**
     * Validates the {@code ownerName} field.
     *
     * @param ownerName owner name string (may be {@code null})
     * @return a {@link ValidationResult}
     */
    public static ValidationResult validateOwnerName(String ownerName) {
        if (isBlank(ownerName)) {
            return ValidationResult.failure("ownerName is required");
        }
        return ownerName.strip().length() >= 3
                ? ValidationResult.success()
                : ValidationResult.failure("ownerName must be at least 3 characters");
    }

    /**
     * Validates the {@code modelName} field.
     *
     * @param modelName model name string (may be {@code null})
     * @return a {@link ValidationResult}
     */
    public static ValidationResult validateModelName(String modelName) {
        if (isBlank(modelName)) {
            return ValidationResult.failure("modelName is required");
        }
        return ValidationResult.success();
    }

    /**
     * Validates the {@code regNumber} field against the required pattern.
     *
     * @param regNumber registration number string (may be {@code null})
     * @return a {@link ValidationResult}
     */
    public static ValidationResult validateRegNumber(String regNumber) {
        if (isBlank(regNumber)) {
            return ValidationResult.failure("regNumber is required");
        }
        return REG_NUMBER_PATTERN.matcher(regNumber.strip()).matches()
                ? ValidationResult.success()
                : ValidationResult.failure(
                        "regNumber must match pattern [A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4} (e.g. TS09AB1234)");
    }

    /**
     * Validates all four vehicle fields in sequence and returns the first
     * failure encountered, or {@link ValidationResult#success()} if all pass.
     *
     * <p>Uses a Java 21 pattern-matching switch expression to dispatch on each
     * field result, demonstrating the modern idiom over legacy if-else chains.
     *
     * @param vehicleIdRaw raw vehicle ID string from request
     * @param ownerName    owner name
     * @param modelName    model name
     * @param regNumber    registration number
     * @return first {@link ValidationResult.Failure} encountered, or {@link ValidationResult.Success}
     */
    public static ValidationResult validateAll(
            String vehicleIdRaw,
            String ownerName,
            String modelName,
            String regNumber) {

        // Evaluated left-to-right; first failure short-circuits
        for (ValidationResult result : new ValidationResult[]{
                validateVehicleId(vehicleIdRaw),
                validateOwnerName(ownerName),
                validateModelName(modelName),
                validateRegNumber(regNumber)
        }) {
            // Java 21 pattern-matching instanceof
            if (result instanceof ValidationResult.Failure f) {
                return f;
            }
        }
        return ValidationResult.success();
    }

    // -------------------------------------------------------------------------
    // Inner sealed type
    // -------------------------------------------------------------------------

    /**
     * ValidationResult — sealed type representing the outcome of a single
     * validation check.
     *
     * <p>Consumers switch exhaustively:
     * <pre>{@code
     * switch (result) {
     *     case ValidationResult.Success s  -> proceed();
     *     case ValidationResult.Failure f  -> rejectWith(f.reason());
     * }
     * }</pre>
     */
    public sealed interface ValidationResult
            permits ValidationResult.Success, ValidationResult.Failure {

        record Success() implements ValidationResult {}

        record Failure(String reason) implements ValidationResult {}

        static ValidationResult success() {
            return new Success();
        }

        static ValidationResult failure(String reason) {
            return new Failure(reason);
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}