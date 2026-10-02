package com.homelab.authservice.security.notification.dto;

import java.util.UUID;

public record UserRegisteredEvent(
        UUID eventId,
        NotificationType notificationType,
        String emailAddress,
        String username
) {
}
