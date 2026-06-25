# Backend Test Cases: Java 21 + Jakarta EE 10

## Overview

Comprehensive test coverage for migrated backend modules covering Vehicle Record, ValidationUtil, VehicleDAO, VehicleServlet, DBConnection, and CorsFilter.

| Test ID   | Module              | Priority | Type        | Status |
| --------- | ------------------- | -------- | ----------- | ------ |
| TC-BE-001 | Vehicle.java        | P0       | Unit        | Ready  |
| TC-BE-002 | Vehicle.java        | P0       | Unit        | Ready  |
| TC-BE-003 | ValidationUtil.java | P0       | Unit        | Ready  |
| TC-BE-004 | ValidationUtil.java | P0       | Unit        | Ready  |
| TC-BE-005 | ValidationUtil.java | P0       | Unit        | Ready  |
| TC-BE-006 | ValidationUtil.java | P0       | Unit        | Ready  |
| TC-BE-007 | VehicleDAO.java     | P0       | Integration | Ready  |
| TC-BE-008 | VehicleDAO.java     | P0       | Integration | Ready  |
| TC-BE-009 | VehicleDAO.java     | P0       | Integration | Ready  |
| TC-BE-010 | VehicleDAO.java     | P0       | Integration | Ready  |
| TC-BE-011 | VehicleDAO.java     | P0       | Integration | Ready  |
| TC-BE-012 | VehicleDAO.java     | P0       | Integration | Ready  |
| TC-BE-013 | VehicleServlet.java | P0       | Integration | Ready  |
| TC-BE-014 | VehicleServlet.java | P0       | Integration | Ready  |
| TC-BE-015 | VehicleServlet.java | P0       | Integration | Ready  |
| TC-BE-016 | VehicleServlet.java | P0       | Integration | Ready  |
| TC-BE-017 | VehicleServlet.java | P0       | Integration | Ready  |
| TC-BE-018 | CorsFilter.java     | P0       | Integration | Ready  |
| TC-BE-019 | DBConnection.java   | P0       | Unit        | Ready  |
| TC-BE-020 | VehicleDAO.java     | P0       | Integration | Ready  |
| TC-BE-021 | ValidationUtil.java | P0       | Unit        | Ready  |
| TC-BE-022 | ValidationUtil.java | P0       | Unit        | Ready  |
| TC-BE-023 | VehicleServlet.java | P0       | Integration | Ready  |
| TC-BE-024 | VehicleServlet.java | P0       | Integration | Ready  |
| TC-BE-025 | VehicleDAO.java     | P0       | Integration | Ready  |
| TC-BE-026 | VehicleServlet.java | P0       | Integration | Ready  |
| TC-BE-027 | VehicleDAO.java     | P0       | Integration | Ready  |
| TC-BE-028 | VehicleDAO.java     | P0       | Integration | Ready  |
| TC-BE-029 | VehicleServlet.java | P0       | Integration | Ready  |
| TC-BE-030 | DBConnection.java   | P0       | Unit        | Ready  |

## Core Test Cases

### TC-BE-001: Vehicle Record Immutability

**Precondition:** Vehicle record instantiated with vehicleId=1, ownerName="John", modelName="Toyota", regNumber="TS09AB1234"
**Steps:** 1. Verify record has no setter methods 2. Attempt field reassignment via reflection
**Expected:** Record remains immutable; no setters exist; IllegalAccessError on reflection attempt

### TC-BE-002: Vehicle JSON Serialization Round-Trip

**Precondition:** Vehicle(123, "Alice", "Honda", "AB12CD3456")
**Steps:** 1. Serialize to JSON 2. Deserialize 3. Compare fields
**Expected:** All 4 fields preserved; equals() returns true

### TC-BE-003: RegNumber Pattern Valid [A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}

**Precondition:** ValidationUtil.isValidRegNumber("TS09AB1234")
**Steps:** 1. Invoke validation
**Expected:** Returns true

### TC-BE-004: RegNumber Pattern Invalid – Length

**Precondition:** Input "TS09AB123" (9 chars)
**Steps:** 1. Call isValidRegNumber()
**Expected:** Returns false

### TC-BE-005: RegNumber Pattern Invalid – Lowercase

**Precondition:** Input "ts09ab1234"
**Steps:** 1. Call isValidRegNumber()
**Expected:** Returns false (uppercase required)

### TC-BE-006: OwnerName Minimum Length 3

**Precondition:** "Joe" (3 chars)
**Steps:** 1. Call isValidOwnerName("Joe")
**Expected:** Returns true

### TC-BE-007: VehicleDAO.addVehicle Success

**Precondition:** Valid Vehicle(999, "TestOwner", "TestModel", "TS09AB9999"); DB connection pooled
**Steps:** 1. Call addVehicle() 2. Execute SELECT for vehicleId=999
**Expected:** INSERT succeeds; record persisted

### TC-BE-008: VehicleDAO.addVehicle Duplicate PK

**Precondition:** Vehicle ID 1 exists; attempt INSERT with same ID
**Steps:** 1. Call addVehicle(new Vehicle(1, ...))
**Expected:** SQLException thrown; original record unchanged

### TC-BE-009: VehicleDAO.getAllVehicles Empty Table

**Precondition:** VEHICLE table is empty
**Steps:** 1. Call getAllVehicles()
**Expected:** Returns empty List (not null)

### TC-BE-010: VehicleDAO.getAllVehicles Multiple Records

**Precondition:** 3 vehicles in DB
**Steps:** 1. Call getAllVehicles() 2. Verify list size and field values
**Expected:** List of 3 vehicles with correct data

### TC-BE-011: VehicleDAO.getVehicleById Found

**Precondition:** Vehicle ID 42 exists
**Steps:** 1. Call getVehicleById(42)
**Expected:** Optional<Vehicle> with correct data

### TC-BE-012: VehicleDAO.getVehicleById Not Found

**Precondition:** Vehicle ID 9999 does not exist
**Steps:** 1. Call getVehicleById(9999)
**Expected:** Optional.empty() returned

### TC-BE-013: VehicleServlet GET /api/vehicles Success

**Precondition:** 2 vehicles in DB
**Steps:** 1. Send HTTP GET /api/vehicles
**Expected:** 200 OK; Content-Type application/json; success=true; data array with 2 vehicles

### TC-BE-014: VehicleServlet POST /api/vehicles Valid

**Precondition:** JSON body: {vehicleId:888, ownerName:"NewOwner", modelName:"BMW", regNumber:"BW00BW8888"}
**Steps:** 1. Send HTTP POST /api/vehicles
**Expected:** 201 Created or 200 OK; Vehicle persisted

### TC-BE-015: VehicleServlet POST Invalid RegNumber

**Precondition:** JSON with regNumber="invalid"
**Steps:** 1. Send HTTP POST /api/vehicles
**Expected:** 400 Bad Request; validation error in response

### TC-BE-016: VehicleServlet PUT /api/vehicles/{id} Success

**Precondition:** Vehicle 50 exists
**Steps:** 1. Send PUT /api/vehicles/50 with updated ownerName
**Expected:** 200 OK; updated Vehicle returned

### TC-BE-017: VehicleServlet DELETE /api/vehicles/{id} Success

**Precondition:** Vehicle 60 exists
**Steps:** 1. Send DELETE /api/vehicles/60 2. Query for ID 60
**Expected:** 200 OK or 204 No Content; record removed

### TC-BE-018: CorsFilter OPTIONS Pre-flight Request

**Precondition:** Browser sends OPTIONS /api/vehicles
**Steps:** 1. Send OPTIONS request
**Expected:** 200 OK; Access-Control-Allow-\* headers present

### TC-BE-019: DBConnection Pool Initialization

**Precondition:** First invocation of DBConnection.getConnection()
**Steps:** 1. Call getConnection()
**Expected:** Connection obtained; pool initialized with configured size

### TC-BE-020: JDBC Resource Leak Prevention – Try-With-Resources

**Precondition:** VehicleDAO.getAllVehicles() with 100+ records
**Steps:** 1. Call getAllVehicles() 2. Monitor connection pool size before/after 3. Call 50 times in loop
**Expected:** All connections released; pool size returns to baseline; no connection leaks detected

### TC-BE-021: Null Input Handling – ValidationUtil.isValidOwnerName(null)

**Precondition:** Input is null
**Steps:** 1. Call isValidOwnerName(null)
**Expected:** Returns false (no NullPointerException)

### TC-BE-022: Boundary Condition – VehicleId = 0

**Precondition:** Input vehicleId=0
**Steps:** 1. Attempt to add vehicle with ID 0
**Expected:** Validation rejects; error response with message "Vehicle ID must be > 0"

### TC-BE-023: Pattern Matching – Vehicle Type Classification

**Precondition:** Vehicle record with modelName="BMW X5"
**Steps:** 1. Use pattern matching switch to classify vehicle type
**Expected:** Correctly identifies luxury vehicle category

### TC-BE-024: Sealed Class Hierarchy – VehicleOperation Status

**Precondition:** Create VehicleOperation sealed class with permitted subclasses
**Steps:** 1. Instantiate CreateOperation, UpdateOperation, DeleteOperation 2. Verify no other subclasses allowed
**Expected:** Only permitted classes can extend; compiler prevents unauthorized subclasses

### TC-BE-025: VehicleDAO.updateVehicle – Partial Update

**Precondition:** Vehicle 30 exists; update only ownerName field
**Steps:** 1. Call updateVehicle(30, partialData) 2. Verify only ownerName changed
**Expected:** UPDATE succeeds; other fields unchanged; no NULL overwrites

### TC-BE-026: VehicleServlet DELETE Non-Existent Vehicle

**Precondition:** Attempt DELETE /api/vehicles/9999 (non-existent)
**Steps:** 1. Send DELETE request
**Expected:** 404 Not Found or 200 with error message; no exception thrown

### TC-BE-027: Concurrent Access – Multiple Threads Adding Vehicles

**Precondition:** 10 threads simultaneously adding vehicles
**Steps:** 1. Launch 10 concurrent addVehicle calls 2. Verify all succeed 3. Count final DB records
**Expected:** All 10 vehicles persisted; no data corruption; connection pool handles concurrency

### TC-BE-028: Stream API Modern Syntax – getAllVehicles().stream()

**Precondition:** 5 vehicles in DB
**Steps:** 1. Call getAllVehicles() 2. Filter by regNumber pattern 3. Collect to List
**Expected:** Stream operations work correctly; .toList() returns immutable list (Java 16+)

### TC-BE-029: VehicleServlet Error Response Format

**Precondition:** POST /api/vehicles with invalid JSON payload
**Steps:** 1. Send malformed JSON 2. Verify error response structure
**Expected:** 400 Bad Request; response contains {success:false, message:"...", errorCode:"..."}

### TC-BE-030: DBConnection Fallback – Oracle Connection Retry

**Precondition:** DB connection fails on first attempt
**Steps:** 1. Mock DB as unavailable 2. Call getConnection() 3. Verify retry logic
**Expected:** Connection retried; exception raised after max retries; meaningful error message
