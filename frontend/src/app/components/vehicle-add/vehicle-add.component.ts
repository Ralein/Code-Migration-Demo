import { Component, inject } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { Router, RouterLink } from "@angular/router";
import { VehicleService } from "../../services/vehicle.service";

/**
 * VehicleAddComponent — handles the registration of new vehicles.
 *
 * Migration note: Replaces addVehicle.jsp + AddVehicleServlet.
 * Validates inputs using Angular Reactive Forms before sending them
 * via POST /api/vehicles to the Java 21 backend.
 */
@Component({
  selector: "app-vehicle-add",
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: "./vehicle-add.component.html",
  styleUrls: ["./vehicle-add.component.css"],
})
export class VehicleAddComponent {
  private readonly fb = inject(FormBuilder);
  private readonly vehicleService = inject(VehicleService);
  private readonly router = inject(Router);

  addForm: FormGroup = this.fb.group({
    vehicleId: [
      "",
      [Validators.required, Validators.min(1), Validators.pattern(/^[0-9]+$/)],
    ],
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

  loading = false;
  errorMessage = "";

  onSubmit(): void {
    if (this.addForm.invalid) {
      this.addForm.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = "";

    const formValue = this.addForm.value;
    const payload = {
      vehicleId: Number(formValue.vehicleId),
      ownerName: formValue.ownerName,
      modelName: formValue.modelName,
      regNumber: formValue.regNumber,
    };

    this.vehicleService.addVehicle(payload).subscribe({
      next: (response) => {
        this.loading = false;
        if (response.success) {
          this.router.navigate(["/vehicles"]);
        } else {
          this.errorMessage = response.message || "Failed to add vehicle.";
        }
      },
      error: (err: Error) => {
        this.loading = false;
        this.errorMessage = err.message;
      },
    });
  }
}
