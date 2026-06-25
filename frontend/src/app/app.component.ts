import { Component } from "@angular/core";
import { RouterLink, RouterLinkActive, RouterOutlet } from "@angular/router";

/**
 * AppComponent — root shell component.
 *
 * Renders the top navigation bar (replacing the legacy JSP index.jsp navigation
 * links) and the router outlet that hosts lazy-loaded feature components.
 */
@Component({
  selector: "app-root",
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"],
})
export class AppComponent {
  title = "Vehicle Service Management System";
}
