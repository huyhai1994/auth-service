package com.homelab.authservice.security.authentication.register.service;

import com.homelab.authservice.security.notification.dto.OutboxEventStatus;
import com.homelab.authservice.security.notification.dto.UserRegisteredEvent;
import com.homelab.authservice.security.notification.entity.OutboxEvent;
import com.homelab.authservice.security.notification.repository.OutBoxEventRepository;
import com.homelab.authservice.shared.json.JacksonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutboxEventDomainPublisher implements DomainEventPublisher {

    private final OutBoxEventRepository outBoxEventRepository;
    private final JacksonUtils jacksonUtils;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void publishEvent(UserRegisteredEvent event) {
        outBoxEventRepository.save(mapFrom(event));
    }

    private OutboxEvent mapFrom(UserRegisteredEvent event) {
        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEventId(event.eventId());
        outboxEvent.setStatus(OutboxEventStatus.PENDING);
        String json = jacksonUtils.convertObjectToJson(event);
        outboxEvent.setPayload(json);
        outboxEvent.setRetryCount(0);
        return outboxEvent;
    }


}
