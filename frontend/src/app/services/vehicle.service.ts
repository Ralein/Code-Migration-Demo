import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpErrorResponse } from "@angular/common/http";
import { Observable, throwError } from "rxjs";
import { catchError } from "rxjs/operators";
import { Vehicle, VehiclePayload, ApiResponse } from "../models/vehicle.model";

/**
 * VehicleService — centralises all HTTP communication with the Java 21 REST backend.
 *
 * Migration note: The legacy application had no service layer on the client side;
 * form submissions went directly to Servlet URLs. This service abstracts all API
 * calls, making each component free of transport concerns.
 *
 * Base URL: /api/vehicles  (proxied to http://localhost:8080 during development
 * via proxy.conf.json; served from the same origin in production)
 */
@Injectable({
  providedIn: "root",
})
export class VehicleService {
  private readonly http = inject(HttpClient);
  private readonly baseUrl = "/api/vehicles";

  /** GET /api/vehicles — retrieve all vehicles */
  getAllVehicles(): Observable<ApiResponse<Vehicle[]>> {
    return this.http
      .get<ApiResponse<Vehicle[]>>(this.baseUrl)
      .pipe(catchError(this.handleError));
  }

  /** GET /api/vehicles/{id} — retrieve a single vehicle by ID */
  getVehicleById(id: number): Observable<ApiResponse<Vehicle>> {
    return this.http
      .get<ApiResponse<Vehicle>>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  /** POST /api/vehicles — add a new vehicle */
  addVehicle(payload: VehiclePayload): Observable<ApiResponse<Vehicle>> {
    return this.http
      .post<ApiResponse<Vehicle>>(this.baseUrl, payload)
      .pipe(catchError(this.handleError));
  }

  /** PUT /api/vehicles/{id} — update an existing vehicle */
  updateVehicle(
    id: number,
    payload: VehiclePayload,
  ): Observable<ApiResponse<Vehicle>> {
    return this.http
      .put<ApiResponse<Vehicle>>(`${this.baseUrl}/${id}`, payload)
      .pipe(catchError(this.handleError));
  }

  /** DELETE /api/vehicles/{id} — delete a vehicle */
  deleteVehicle(id: number): Observable<ApiResponse<null>> {
    return this.http
      .delete<ApiResponse<null>>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  /** Search by vehicle ID — delegates to getVehicleById */
  searchVehicle(id: number): Observable<ApiResponse<Vehicle>> {
    return this.getVehicleById(id);
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    const message =
      error.error instanceof ErrorEvent
        ? error.error.message
        : `Server error ${error.status}: ${error.message}`;
    return throwError(() => new Error(message));
  }
}
