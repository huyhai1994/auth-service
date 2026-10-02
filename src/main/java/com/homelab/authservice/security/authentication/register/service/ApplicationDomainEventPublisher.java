package com.homelab.authservice.security.authentication.register.service;

import com.homelab.authservice.security.notification.dto.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationDomainEventPublisher implements DomainEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishEvent(UserRegisteredEvent event) {
        eventPublisher.publishEvent(event);
    }
}
