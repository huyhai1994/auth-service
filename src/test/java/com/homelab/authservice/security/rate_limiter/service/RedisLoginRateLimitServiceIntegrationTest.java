package com.homelab.authservice.security.rate_limiter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import support.AbstractIntegrationTest;
import support.RaceConditionSimulator;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@ActiveProfiles({"test"})
class RedisLoginRateLimitServiceIntegrationTest
        extends AbstractIntegrationTest {

    @Autowired
    RedisLoginRateLimitService service;

    @Test
    void allow_whenEleventConcurrentRequests_thenAllowOnlyTen()
            throws Exception {

        String identity =
                "test-identity-" + UUID.randomUUID();

        try (RaceConditionSimulator simulator =
                     RaceConditionSimulator
                             .getRaceConditionSimulator(11)) {

            List<Boolean> results = simulator.execute(
                    () -> service.allow(identity)
            );

            assertThat(results)
                    .hasSize(11);

            assertThat(results)
                    .filteredOn(Boolean.TRUE::equals)
                    .hasSize(10);

            assertThat(results)
                    .filteredOn(Boolean.FALSE::equals)
                    .hasSize(1);
        }
    }


}