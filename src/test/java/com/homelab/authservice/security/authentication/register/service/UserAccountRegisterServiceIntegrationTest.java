package com.homelab.authservice.security.authentication.register.service;

import com.homelab.authservice.security.authentication.register.dto.RegisterRequest;
import com.homelab.authservice.security.authentication.shared.entity.User;
import com.homelab.authservice.security.authentication.shared.repository.RoleRepository;
import com.homelab.authservice.security.authentication.shared.repository.UserRepository;
import com.homelab.authservice.security.notification.dto.OutboxEventStatus;
import com.homelab.authservice.security.notification.entity.OutboxEvent;
import com.homelab.authservice.security.notification.repository.OutBoxEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import support.AbstractIntegrationTest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static support.MockPasswordBuilder.VALID_PASSWORD;
import static support.MockUserBuilder.DEFAULT_USERNAME;
import static support.MockUserBuilder.VALID_EMAIL;

@SpringBootTest
@ActiveProfiles("test")
class UserAccountRegisterServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    UserAccountRegisterService userAccountRegisterService;

    @Autowired
    UserRepository userRepository;

    @MockitoSpyBean
    OutBoxEventRepository outBoxEventRepository;

    @Autowired
    RoleRepository roleRepository;

    @BeforeEach
    void cleanUp() {
        outBoxEventRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    void save_whenRoleNotPersist_thenUserNotSaved() {
        RegisterRequest request = validRegisterRequest();

        roleRepository.deleteAllInBatch();
        assertThat(roleRepository.count()).isZero();
        assertThatThrownBy(() -> userAccountRegisterService.register(request));

        assertThat(userRepository.findAll().size()).isZero();
        List<OutboxEvent> events = outBoxEventRepository.findAll();
        assertThat(events.size()).isZero();

    }

    @Test
    void save_whenUserAccountSaved_thenOutboxEventSaved() {

        RegisterRequest request = validRegisterRequest();

        userAccountRegisterService.register(request);

        List<OutboxEvent> events = outBoxEventRepository.findAll();
        assertThat(userRepository.findAll().get(0).getEmailAddress()).isEqualTo(request.emailAddress());
        assertThat((long) events.size()).isOne();
        assertThat(events.get(0).getPayload()).isNotNull();
        assertThat(events.get(0).getStatus()).isEqualTo(OutboxEventStatus.PENDING);
        assertThat(events.get(0).getCreatedAt()).isNotNull();
    }


    @Test
    void register_whenSavingOutboxEventFails_thenRollbackUser() {

        RegisterRequest request = validRegisterRequest();

        doThrow(new RuntimeException("Outbox database error"))
                .when(outBoxEventRepository)
                .save(any(OutboxEvent.class));

        assertThatThrownBy(() ->
                userAccountRegisterService.register(request)
        )
                .isInstanceOf(RuntimeException.class);

        assertThat(userRepository.findAll()).isEmpty();
        assertThat(outBoxEventRepository.findAll()).isEmpty();
    }

    private RegisterRequest validRegisterRequest() {
        return registerRequest(
                DEFAULT_USERNAME,
                VALID_PASSWORD,
                VALID_EMAIL
        );
    }

    private RegisterRequest registerRequest(
            String username,
            String password,
            String email
    ) {
        return new RegisterRequest(username, password, email);
    }
}