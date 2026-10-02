package com.homelab.authservice.security.authentication.register.dto;

import java.util.UUID;

public record RegisterResponse(UUID id, String username) {
}
