import { Component, OnInit, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterLink } from "@angular/router";
import { VehicleService } from "../../services/vehicle.service";
import { Vehicle } from "../../models/vehicle.model";

/**
 * VehicleListComponent — displays all vehicles in a tabular layout.
 *
 * Migration note: Replaces viewVehicles.jsp + ViewVehiclesServlet.
 * Data is fetched from GET /api/vehicles instead of being rendered
 * server-side by a JSP template.
 */
@Component({
  selector: "app-vehicle-list",
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: "./vehicle-list.component.html",
  styleUrls: ["./vehicle-list.component.css"],
})
export class VehicleListComponent implements OnInit {
  private readonly vehicleService = inject(VehicleService);

  vehicles: Vehicle[] = [];
  loading = false;
  errorMessage = "";
  successMessage = "";

  ngOnInit(): void {
    this.loadVehicles();
  }

  loadVehicles(): void {
    this.loading = true;
    this.errorMessage = "";
    this.vehicleService.getAllVehicles().subscribe({
      next: (response) => {
        this.loading = false;
        if (response.success && response.data) {
          this.vehicles = response.data;
        } else {
          this.errorMessage = response.message || "Failed to load vehicles.";
        }
      },
      error: (err: Error) => {
        this.loading = false;
        this.errorMessage = err.message;
      },
    });
  }

  deleteVehicle(id: number): void {
    if (!confirm(`Delete vehicle ID ${id}? This action cannot be undone.`)) {
      return;
    }
    this.vehicleService.deleteVehicle(id).subscribe({
      next: (response) => {
        if (response.success) {
          this.successMessage = `Vehicle ${id} deleted successfully.`;
          this.vehicles = this.vehicles.filter((v) => v.vehicleId !== id);
        } else {
          this.errorMessage = response.message;
        }
      },
      error: (err: Error) => {
        this.errorMessage = err.message;
      },
    });
  }
}
