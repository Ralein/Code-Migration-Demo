# Frontend Test Cases: Angular 21

## Overview

Test coverage for Angular 21 components, services, and routing. Modules: vehicle.service.ts, vehicle-list.component.ts, vehicle-add.component.ts, vehicle-edit.component.ts, vehicle-search.component.ts, app.routes.ts.

| Test ID   | Module                      | Priority | Type      | Status |
| --------- | --------------------------- | -------- | --------- | ------ |
| TC-FE-001 | vehicle.service.ts          | P0       | Unit      | Ready  |
| TC-FE-002 | vehicle.service.ts          | P0       | Unit      | Ready  |
| TC-FE-003 | vehicle.service.ts          | P0       | Unit      | Ready  |
| TC-FE-004 | vehicle.service.ts          | P0       | Unit      | Ready  |
| TC-FE-005 | vehicle-list.component.ts   | P0       | Component | Ready  |
| TC-FE-006 | vehicle-list.component.ts   | P0       | Component | Ready  |
| TC-FE-007 | vehicle-list.component.ts   | P0       | Component | Ready  |
| TC-FE-008 | vehicle-add.component.ts    | P0       | Component | Ready  |
| TC-FE-009 | vehicle-add.component.ts    | P0       | Component | Ready  |
| TC-FE-010 | vehicle-edit.component.ts   | P0       | Component | Ready  |
| TC-FE-011 | vehicle-search.component.ts | P0       | Component | Ready  |
| TC-FE-012 | app.routes.ts               | P0       | Routing   | Ready  |
| TC-FE-013 | app.routes.ts               | P0       | Routing   | Ready  |
| TC-FE-014 | vehicle.service.ts          | P0       | Unit      | Ready  |
| TC-FE-015 | vehicle-add.component.ts    | P0       | Component | Ready  |
| TC-FE-016 | vehicle-list.component.ts   | P0       | Component | Ready  |
| TC-FE-017 | vehicle-edit.component.ts   | P0       | Component | Ready  |
| TC-FE-018 | vehicle-search.component.ts | P0       | Component | Ready  |
| TC-FE-019 | vehicle.service.ts          | P0       | Unit      | Ready  |
| TC-FE-020 | app.component.ts            | P0       | Component | Ready  |
| TC-FE-021 | vehicle-list.component.ts   | P0       | Component | Ready  |
| TC-FE-022 | vehicle-add.component.ts    | P0       | Component | Ready  |
| TC-FE-023 | vehicle.service.ts          | P0       | Unit      | Ready  |
| TC-FE-024 | vehicle-list.component.ts   | P0       | Component | Ready  |
| TC-FE-025 | vehicle-edit.component.ts   | P0       | Component | Ready  |

## Core Test Cases

### TC-FE-001: VehicleService.getAllVehicles Success

**Precondition:** HttpClient mocked; returns {success:true, data:[2 vehicles]}
**Steps:** 1. Call getAllVehicles() 2. Subscribe and verify
**Expected:** Observable emits array with 2 vehicles; no error

### TC-FE-002: VehicleService.getAllVehicles HTTP 500

**Precondition:** HttpClient returns 500 error
**Steps:** 1. Call getAllVehicles() 2. Handle error
**Expected:** Observable throws; error handler invoked

### TC-FE-003: VehicleService.getVehicleById Found

**Precondition:** HttpClient returns vehicle for ID 42
**Steps:** 1. Call getVehicleById(42)
**Expected:** Observable emits Vehicle with ID 42

### TC-FE-004: VehicleService.addVehicle Valid

**Precondition:** Valid payload {vehicleId:999, ownerName:"Owner", modelName:"Model", regNumber:"TS09AB9999"}
**Steps:** 1. Call addVehicle(payload)
**Expected:** HTTP POST sent; response contains created Vehicle

### TC-FE-005: VehicleListComponent ngOnInit

**Precondition:** Component initialized; VehicleService mocked
**Steps:** 1. Component loads 2. Verify loadVehicles() called
**Expected:** vehicles array populated; loading=false

### TC-FE-006: VehicleListComponent Delete Success

**Precondition:** 3 vehicles in state; delete ID 1
**Steps:** 1. Click delete 2. Confirm 3. Wait response
**Expected:** Vehicle removed; success message shown

### TC-FE-007: VehicleListComponent Delete Error

**Precondition:** deleteVehicle service fails
**Steps:** 1. Click delete 2. Service returns error
**Expected:** errorMessage populated; vehicle remains

### TC-FE-008: VehicleAddComponent Form Submit Valid

**Precondition:** Form complete with valid data
**Steps:** 1. Click Submit 2. Verify service called 3. Check navigation
**Expected:** POST succeeds; user routed to /vehicles; notification shown

### TC-FE-009: VehicleAddComponent Form Validation Error

**Precondition:** regNumber field empty
**Steps:** 1. Click Submit
**Expected:** Form invalid; button disabled; error shown on field

### TC-FE-010: VehicleEditComponent Load and Update

**Precondition:** Route param :id=50; vehicle exists
**Steps:** 1. Load component 2. Form pre-populates 3. Submit update
**Expected:** Current values shown; PUT sent; success message displayed

### TC-FE-011: VehicleSearchComponent Search by ID

**Precondition:** User enters vehicleId=42
**Steps:** 1. Submit search 2. Service fetches vehicle
**Expected:** Result displayed or "Not Found" message

### TC-FE-012: AppRoutes Default Redirect

**Precondition:** Navigate to /
**Steps:** 1. Go to /
**Expected:** Redirects to /vehicles

### TC-FE-013: AppRoutes Wildcard Not Found

**Precondition:** Navigate to /invalid-route
**Steps:** 1. Go to /invalid-route
**Expected:** Wildcard redirects to /vehicles

### TC-FE-014: VehicleService Unsubscribe Memory Leak

**Precondition:** Component subscribes to getAllVehicles()
**Steps:** 1. Subscribe 2. Destroy component 3. Verify unsubscribe
**Expected:** Subscription destroyed; no memory leaks

### TC-FE-015: VehicleAddComponent RegNumber Format Live Validation

**Precondition:** Form with real-time validation
**Steps:** 1. Type invalid "ABC" 2. Error shown 3. Correct to "TS09AB1234"
**Expected:** Error disappears; Submit enabled

### TC-FE-016: VehicleListComponent Pagination 100+ Vehicles

**Precondition:** DB has 150 vehicles
**Steps:** 1. Load component 2. Verify pagination 3. Navigate pages
**Expected:** Pagination shown; correct subset per page; no lag

### TC-FE-017: VehicleEditComponent Unsaved Changes Warning

**Precondition:** User modifies form
**Steps:** 1. Change ownerName 2. Navigate away
**Expected:** Browser confirms unsaved; prevents navigation

### TC-FE-018: VehicleSearchComponent Empty Input

**Precondition:** vehicleId field empty
**Steps:** 1. Click Search
**Expected:** Validation prevents submission; error shown

### TC-FE-019: VehicleService HttpClient Timeout

**Precondition:** Request times out after 5 seconds
**Steps:** 1. Call getAllVehicles() 2. Wait 6+ seconds
**Expected:** Observable emits timeout error; component shows message

### TC-FE-020: AppComponent Responsive Navigation

**Precondition:** Window width < 768px (mobile)
**Steps:** 1. Load on mobile 2. Hamburger menu appears 3. Click to toggle
**Expected:** Menu toggles; links functional on mobile

### TC-FE-021: VehicleListComponent Sort by Column

**Precondition:** List displayed with owner names
**Steps:** 1. Click "Owner Name" column header 2. Sort ascending 3. Click descending
**Expected:** Table reorders; sort indicator shows direction

### TC-FE-022: VehicleAddComponent Reset Form

**Precondition:** Form filled with data
**Steps:** 1. Click Reset button
**Expected:** All fields cleared; form returns to initial state

### TC-FE-023: VehicleService Global Error Handler Interceptor

**Precondition:** Any HTTP call returns 401 Unauthorized
**Steps:** 1. Make request 2. Interceptor catches 401 3. Verify redirect
**Expected:** User redirected to /login; error logged; session cleared

### TC-FE-024: VehicleListComponent Infinite Scroll Load More

**Precondition:** 200 vehicles; infinite scroll enabled
**Steps:** 1. Scroll to bottom 2. Verify more load automatically
**Expected:** Batch loaded; no duplicates; smooth UX

### TC-FE-025: VehicleEditComponent Concurrent Update Detection

**Precondition:** User A and User B edit same vehicle simultaneously
**Steps:** 1. Both load vehicle 5 2. User A saves 3. User B attempts save
**Expected:** Conflict detected; merge UI shown; no overwrite
