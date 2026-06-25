# MIGRATION-2025 — Java 8 + JSP to Java 21 + Angular 17

Migrate VehicleServiceApp from Java 8 + JSP + Servlets to Java 21 + Jakarta EE 10 REST backend with Angular 17 SPA frontend. Fix critical JDBC resource leak in VehicleDAO.getAllVehicles(); replace rs.stream().map() with explicit while loop to prevent connection pool exhaustion. Upgrade javax.servlet to jakarta.servlet namespace; introduce modern Java constructs (Records, pattern matching, sealed classes).

## What

Legacy monolithic app (Java 8, JSP, Servlets, JDBC, Tomcat 9, Oracle 21c) binds developers to Java 8 security fixes only; framework incompatible with Java 17+. Frontend tightly coupled to server-side rendering (JSP). JDBC resource leak in getAllVehicles() via ResultSet.stream().map()—exception in lambda bypasses try-with-resources finally, exhausting connection pool under load.

## Fix

- **backend/dao/VehicleDAO.java:** Replace rs.stream().map().toList() with explicit while (rs.next()) loop. Deterministic resource closure within try-with-resources scope.
- **backend/filter/CorsFilter.java:** Wrap chain.doFilter() with try-catch for ServletException safety.
- **backend/pom.xml:** Update to Java 21, Jakarta EE 10, JUnit 5.
- **backend/test/dao/VehicleDAOTest.java:** NEW—JUnit 5 CRUD tests + resource leak verification.

## Testing

1. **Setup Oracle schema:**

   ```sql
   CREATE TABLE VEHICLE(VEHICLE_ID NUMBER PRIMARY KEY,OWNER_NAME VARCHAR2(100) NOT NULL,MODEL_NAME VARCHAR2(100) NOT NULL,REG_NUMBER VARCHAR2(20) NOT NULL);
   INSERT INTO VEHICLE VALUES(1,'John Doe','Toyota Camry','TS09AB1234');
   ```

2. **Verify compilation on Java 21:**

   ```bash
   cd backend && mvn clean compile
   ```

3. **Test getAllVehicles() returns correct JSON:**

   ```bash
   curl -X GET http://localhost:8080/api/vehicles
   ```

   **Expected:** HTTP 200 + `{"success":true,"data":[...]}`

4. **Resource leak verification:**
   - Start 50 concurrent GET /api/vehicles requests; monitor DB connection pool.
   - Before fix: Pool exhausts (~30s).
   - After fix: Pool stable; all connections released.

**Post-Deploy Remediation:** None required. Old connections drain naturally on app restart.

## Unit Tests

```bash
cd backend && mvn clean test -Dtest=VehicleDAOTest
```
