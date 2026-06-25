package com.automotive.util;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ApiResponse — generic JSON envelope returned by every REST endpoint.
 *
 * <p><b>Migration note (Java 8 → Java 21):</b>
 * The legacy servlets returned raw HTML or issued HTTP redirects.
 * Every modern endpoint now returns a stable JSON contract so the Angular
 * frontend can rely on a consistent response shape.
 *
 * <p>Fields:
 * <ul>
 *   <li>{@code success} — {@code true} when the operation completed without error.</li>
 *   <li>{@code message} — human-readable status message.</li>
 *   <li>{@code data}    — optional payload; omitted from JSON when {@code null}.</li>
 * </ul>
 *
 * @param <T> type of the optional response payload
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String  message,
        T       data
) {
    /** Convenience factory — success with a payload. */
    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    /** Convenience factory — success without a payload (e.g. delete). */
    public static <T> ApiResponse<T> ok(String message) {
        return new ApiResponse<>(true, message, null);
    }

    /** Convenience factory — failure with a reason message. */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}