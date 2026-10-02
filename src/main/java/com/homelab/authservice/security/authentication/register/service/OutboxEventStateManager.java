package com.homelab.authservice.security.authentication.register.service;

import com.homelab.authservice.security.notification.entity.OutboxEvent;
import com.homelab.authservice.security.notification.repository.OutBoxEventRepository;
import com.homelab.authservice.shared.exception.InvalidStateTransitionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OutboxEventStateManager {
    private final OutBoxEventRepository outBoxEventRepository;
    private final Clock clock;
    private static final int MAX_RETRY = 3;

    @Transactional
    public void markProcessing(Long id) {
        int claims = outBoxEventRepository.markProcessing(id, Instant.now(clock));
        isValidClaims(claims);
    }

    @Transactional
    public void markComplete(Long id) {
        int claims = outBoxEventRepository.markCompleted(id, Instant.now(clock));
        isValidClaims(claims);
    }

    @Transactional
    public void handlePublishFailure(Long id) {

        OutboxEvent event = outBoxEventRepository.findById(id)
                .orElseThrow();

        int claims;
        if (event.getRetryCount() >= MAX_RETRY) {
            claims = outBoxEventRepository.markFailed(id, Instant.now(clock));
        } else {
            claims = outBoxEventRepository.retryEvent(id, Instant.now(clock));
        }
        isValidClaims(claims);
    }

    private static void isValidClaims(int claims) {
        if (claims == 0) throw new InvalidStateTransitionException();
    }
}
