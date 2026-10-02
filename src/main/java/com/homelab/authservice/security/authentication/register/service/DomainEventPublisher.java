package com.homelab.authservice.security.authentication.register.service;

import com.homelab.authservice.security.notification.dto.UserRegisteredEvent;

public interface DomainEventPublisher {
    void publishEvent(UserRegisteredEvent event);
}
