package com.todotask.backend.dto;

public record ErrorResponse(
        String message,
        String details,
        int status
) {
}
