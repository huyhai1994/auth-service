package com.homelab.authservice.security.authentication.register.service;

import com.homelab.authservice.security.authentication.register.components.UserRegistrationFactory;
import com.homelab.authservice.security.authentication.register.dto.RegisterRequest;
import com.homelab.authservice.security.authentication.register.dto.RegisterResponse;
import com.homelab.authservice.security.authentication.register.exception.UsernameAlreadyExistsException;
import com.homelab.authservice.security.authentication.shared.entity.User;
import com.homelab.authservice.security.authentication.shared.repository.UserRepository;
import com.homelab.authservice.security.notification.dto.NotificationType;
import com.homelab.authservice.security.notification.dto.UserRegisteredEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserAccountRegisterService {

    private final UserRepository userRepository;
    private final DomainEventPublisher eventPublisher;
    private final UserRegistrationFactory userRegistrationFactory;

    public UserAccountRegisterService(
            UserRepository userRepository,
            @Qualifier("outboxEventDomainPublisher")
            DomainEventPublisher eventPublisher,
            UserRegistrationFactory userRegistrationFactory
    ) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.userRegistrationFactory = userRegistrationFactory;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        User user = userRegistrationFactory.createUser(request);
        final User persistedUser = persistUser(user);
        publishRegisteredEvent(persistedUser);
        return createRegisterResponse(persistedUser);
    }

    private RegisterResponse createRegisterResponse(User persistedUser) {
        return new RegisterResponse(
                persistedUser.getId(),
                persistedUser.getUsername()
        );
    }

    private void publishRegisteredEvent(User savedUser) {
        eventPublisher.publishEvent(
                createEvent(savedUser)
        );
    }

    private UserRegisteredEvent createEvent(User savedUser) {
        return new UserRegisteredEvent(
                UUID.randomUUID(),
                NotificationType.WELCOME_EMAIL,
                savedUser.getEmailAddress(),
                savedUser.getUsername()
        );
    }

    private User persistUser(User user) {
        final User savedUser;
        try {
            savedUser = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
        return savedUser;
    }
}
