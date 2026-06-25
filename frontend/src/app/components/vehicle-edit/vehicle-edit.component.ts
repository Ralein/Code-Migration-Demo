import { Component, OnInit, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ActivatedRoute, Router, RouterLink } from "@angular/router";
import { VehicleService } from "../../services/vehicle.service";

/**
 * VehicleEditComponent — handles modifying existing vehicles.
 *
 * Migration note: Replaces editVehicle.jsp + UpdateVehicleServlet.
 * Validates edited inputs using Angular Reactive Forms before sending them
 * via PUT /api/vehicles/{id} to the Java 21 REST backend.
 */
@Component({
  selector: "app-vehicle-edit",
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: "./vehicle-edit.component.html",
  styleUrls: ["./vehicle-edit.component.css"],
})
export class VehicleEditComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly vehicleService = inject(VehicleService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  editForm: FormGroup = this.fb.group({
    vehicleId: [{ value: "", disabled: true }],
    ownerName: ["", [Validators.required, Validators.minLength(3)]],
    modelName: ["", [Validators.required]],
    regNumber: [
      "",
      [
        Validators.required,
        Validators.pattern(/^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$/),
      ],
    ],
  });

  vehicleId!: number;
  loading = false;
  saving = false;
  errorMessage = "";

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get("id");
    if (idParam) {
      this.vehicleId = Number(idParam);
      this.loadVehicle();
    } else {
      this.errorMessage = "Invalid vehicle ID.";
    }
  }

  loadVehicle(): void {
    this.loading = true;
    this.vehicleService.getVehicleById(this.vehicleId).subscribe({
      next: (response) => {
        this.loading = false;
        if (response.success && response.data) {
          const vehicle = response.data;
          this.editForm.patchValue({
            vehicleId: vehicle.vehicleId,
            ownerName: vehicle.ownerName,
            modelName: vehicle.modelName,
            regNumber: vehicle.regNumber,
          });
        } else {
          this.errorMessage = response.message || "Failed to load vehicle details.";
        }
      },
      error: (err: Error) => {
        this.loading = false;
        this.errorMessage = err.message;
      },
    });
  }

  onSubmit(): void {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    this.saving = true;
    this.errorMessage = "";

    const formValue = this.editForm.getRawValue();
    const payload = {
      vehicleId: this.vehicleId,
      ownerName: formValue.ownerName,
      modelName: formValue.modelName,
      regNumber: formValue.regNumber,
    };

    this.vehicleService.updateVehicle(this.vehicleId, payload).subscribe({
      next: (response) => {
        this.saving = false;
        if (response.success) {
          this.router.navigate(["/vehicles"]);
        } else {
          this.errorMessage = response.message || "Failed to update vehicle.";
        }
      },
      error: (err: Error) => {
        this.saving = false;
        this.errorMessage = err.message;
      },
    });
  }
}
