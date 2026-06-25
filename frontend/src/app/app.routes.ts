import { Routes } from "@angular/router";

/**
 * Application route definitions.
 *
 * Migration note: The legacy application used server-side JSP pages accessed via
 * direct URL paths (e.g. /VehicleServiceApp/viewVehicles, /addVehicle.jsp).
 * These are replaced by Angular client-side routes that lazily load standalone
 * components. The backend no longer serves view templates — it exposes only the
 * REST API at /api/vehicles/*.
 */
export const routes: Routes = [
  {
    path: "",
    redirectTo: "vehicles",
    pathMatch: "full",
  },
  {
    path: "vehicles",
    loadComponent: () =>
      import("./components/vehicle-list/vehicle-list.component").then(
        (m) => m.VehicleListComponent,
      ),
    title: "All Vehicles",
  },
  {
    path: "vehicles/add",
    loadComponent: () =>
      import("./components/vehicle-add/vehicle-add.component").then(
        (m) => m.VehicleAddComponent,
      ),
    title: "Add Vehicle",
  },
  {
    path: "vehicles/edit/:id",
    loadComponent: () =>
      import("./components/vehicle-edit/vehicle-edit.component").then(
        (m) => m.VehicleEditComponent,
      ),
    title: "Edit Vehicle",
  },
  {
    path: "vehicles/search",
    loadComponent: () =>
      import("./components/vehicle-search/vehicle-search.component").then(
        (m) => m.VehicleSearchComponent,
      ),
    title: "Search Vehicle",
  },
  {
    path: "**",
    redirectTo: "vehicles",
  },
];
