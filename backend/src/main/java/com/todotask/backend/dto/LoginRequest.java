package com.todotask.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "email is required.")
        @Email(message = "Email should be valid.")
        String email,
        @NotBlank(message = "password is required.")
        @Size(message = "password should be at least 8 character long.")
        String password
) {
}
