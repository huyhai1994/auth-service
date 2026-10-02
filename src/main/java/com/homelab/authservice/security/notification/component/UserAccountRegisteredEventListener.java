package com.homelab.authservice.security.notification.component;

import com.homelab.authservice.security.notification.dto.UserRegisteredEvent;
import com.homelab.authservice.security.notification.service.NotificationServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAccountRegisteredEventListener {

    private final NotificationServiceClient notificationClient;

    @Async("notificationTaskExecutor")
    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handle(UserRegisteredEvent event) {
        log.info("NOTIFICATION_REQUEST={}", event);

        try {
            notificationClient.send(event);
            log.info(
                    "Welcome notification sent, eventId={}",
                    event.eventId()
            );
        } catch (Exception exception) {
            log.error(
                    "Failed to send welcome notification, " +
                            "eventId={}",
                    event.eventId(),
                    exception
            );
        }
    }
}