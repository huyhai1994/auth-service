package com.homelab.authservice.security.authentication.register.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record RegisterRequest(@NonNull @NotBlank String username,
                              @NonNull @NotBlank String password,
                              @NonNull @NotBlank String emailAddress) {

}
