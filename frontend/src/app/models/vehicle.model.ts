/**
 * Vehicle model — TypeScript interface mirroring the Java 21 Vehicle record.
 *
 * Java record (backend):
 *   record Vehicle(long vehicleId, String ownerName, String modelName, String regNumber)
 *
 * The field names match the JSON keys produced by the backend servlet so that
 * Angular's HttpClient can deserialise responses without a custom mapper.
 *
 * Migration note: The legacy JSP/Servlet stack transmitted data as HTML form
 * parameters. The new stack exchanges clean JSON payloads over REST.
 */
export interface Vehicle {
  vehicleId: number;
  ownerName: string;
  modelName: string;
  regNumber: string;
}

/**
 * Payload used for create and update operations.
 * vehicleId is optional on create (supplied by the user) but required on update.
 */
export interface VehiclePayload {
  vehicleId?: number;
  ownerName: string;
  modelName: string;
  regNumber: string;
}

/**
 * Generic API response wrapper matching the backend ApiResponse<T> record.
 */
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T | null;
}
