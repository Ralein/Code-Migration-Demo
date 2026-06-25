import { Component, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { VehicleService } from "../../services/vehicle.service";
import { Vehicle } from "../../models/vehicle.model";
import { RouterLink } from "@angular/router";

/**
 * VehicleSearchComponent — handles querying specific vehicles by ID.
 *
 * Migration note: Replaces searchVehicle.jsp + searchResult.jsp + SearchVehicleServlet.
 * Validates search IDs, requests vehicle details from GET /api/vehicles/{id},
 * and displays the matching result in a clean details view.
 */
@Component({
  selector: "app-vehicle-search",
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: "./vehicle-search.component.html",
  styleUrls: ["./vehicle-search.component.css"],
})
export class VehicleSearchComponent {
  private readonly fb = inject(FormBuilder);
  private readonly vehicleService = inject(VehicleService);

  searchForm: FormGroup = this.fb.group({
    vehicleId: [
      "",
      [Validators.required, Validators.min(1), Validators.pattern(/^[0-9]+$/)],
    ],
  });

  searchedVehicle: Vehicle | null = null;
  loading = false;
  hasSearched = false;
  errorMessage = "";

  onSubmit(): void {
    if (this.searchForm.invalid) {
      this.searchForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = "";
    this.searchedVehicle = null;
    this.hasSearched = false;

    const id = Number(this.searchForm.value.vehicleId);

    this.vehicleService.searchVehicle(id).subscribe({
      next: (response) => {
        this.loading = false;
        this.hasSearched = true;
        if (response.success && response.data) {
          this.searchedVehicle = response.data;
        } else {
          this.errorMessage = response.message || "Vehicle not found.";
        }
      },
      error: (err: Error) => {
        this.loading = false;
        this.hasSearched = true;
        this.errorMessage = err.message || "Failed to search for vehicle.";
      },
    });
  }
}
