package com.todotask.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record TaskRequest(
        @NotBlank(message = "Title is required.")
        String title,
        String description
) {
}
