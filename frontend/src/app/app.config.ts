import { ApplicationConfig } from "@angular/core";
import { provideRouter } from "@angular/router";
import { provideHttpClient } from "@angular/common/http";
import { routes } from "./app.routes";

/**
 * appConfig — root application configuration using Angular 17 standalone API.
 *
 * Migration note: The legacy application had no Angular frontend. This replaces
 * the JSP-based server-side rendering with a decoupled SPA communicating with
 * the Java 21 REST backend via HttpClient.
 */
export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideHttpClient()],
};
