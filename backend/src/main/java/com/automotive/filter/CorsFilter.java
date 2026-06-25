package com.automotive.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * CorsFilter — adds Cross-Origin Resource Sharing headers to every API response.
 *
 * <p><b>Migration note:</b> The legacy JSP/Servlet monolith had no CORS concerns
 * because the browser and server shared the same origin. In the new decoupled
 * architecture the Angular SPA (http://localhost:4200) calls the Jakarta EE
 * backend (http://localhost:8080), so CORS headers are mandatory.
 *
 * <p>The filter uses the {@code jakarta.servlet.*} namespace (Jakarta EE 10 /
 * Servlet 6.0) rather than the legacy {@code javax.servlet.*} namespace.
 *
 * <p>Applies to: all paths under {@code /api/*}.
 */
@WebFilter("/api/*")
public class CorsFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(CorsFilter.class.getName());

    /** Allowed Angular dev-server origin. Override via init-param in web.xml for production. */
    private String allowedOrigin = "http://localhost:4200";

    @Override
    public void init(FilterConfig config) throws ServletException {
        String origin = config.getInitParameter("allowedOrigin");
        if (origin != null && !origin.isBlank()) {
            allowedOrigin = origin;
        }
        LOG.info("CorsFilter initialised — allowedOrigin=" + allowedOrigin);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  httpReq = (HttpServletRequest)  request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        httpRes.setHeader("Access-Control-Allow-Origin",  allowedOrigin);
        httpRes.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpRes.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
        httpRes.setHeader("Access-Control-Max-Age",       "3600");

        // Pre-flight requests must be short-circuited here
        if ("OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
            httpRes.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nothing to release
    }
}