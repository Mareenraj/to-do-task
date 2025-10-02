package com.todotask.backend.dto;

public record TaskResponse(
        Long id,
        String title,
        String description,
        boolean isDone
) {
}
