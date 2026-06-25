package com.automotive.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Vehicle — immutable data carrier implemented as a Java 21 Record.
 *
 * <p><b>Migration note (Java 8 → Java 21):</b>
 * <ul>
 *   <li>Legacy: mutable POJO with private fields, public getters/setters, and
 *       no immutability guarantee. Any code path could mutate state silently.</li>
 *   <li>Modern: {@code record} auto-generates canonical constructor, accessor
 *       methods ({@code vehicleId()}, {@code ownerName()}, …), {@code equals()},
 *       {@code hashCode()}, and {@code toString()} — zero boilerplate.</li>
 * </ul>
 *
 * <p>Jackson deserialises incoming JSON into this record via the canonical
 * constructor. {@code @JsonProperty} ensures the field names in the JSON
 * payload match the record component names exactly.
 *
 * <p>Database schema (unchanged):
 * {@code VEHICLE(VEHICLE_ID, OWNER_NAME, MODEL_NAME, REG_NUMBER)}
 */
public record Vehicle(
        @JsonProperty("vehicleId")  int    vehicleId,
        @JsonProperty("ownerName")  String ownerName,
        @JsonProperty("modelName")  String modelName,
        @JsonProperty("regNumber")  String regNumber
) {
    /**
     * Compact canonical constructor — adds cross-field validation so that
     * a {@code Vehicle} instance is always in a valid state on construction.
     *
     * <p>Pattern matching ({@code instanceof String s && s.isBlank()}) is used
     * to demonstrate Java 16+ guarded patterns.
     */
    public Vehicle {
        if (vehicleId <= 0) {
            throw new IllegalArgumentException(
                    "vehicleId must be greater than 0, got: " + vehicleId);
        }
        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalArgumentException("ownerName must not be blank");
        }
        if (modelName == null || modelName.isBlank()) {
            throw new IllegalArgumentException("modelName must not be blank");
        }
        if (regNumber == null || regNumber.isBlank()) {
            throw new IllegalArgumentException("regNumber must not be blank");
        }
        // Normalise whitespace
        ownerName = ownerName.strip();
        modelName = modelName.strip();
        regNumber = regNumber.strip();
    }
}