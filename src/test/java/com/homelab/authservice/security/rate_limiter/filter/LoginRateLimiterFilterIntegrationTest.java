package com.homelab.authservice.security.rate_limiter.filter;

import com.homelab.authservice.security.rate_limiter.repository.LoginRateLimitRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import support.AbstractIntegrationTest;

@SpringBootTest
@ActiveProfiles("test")
class LoginRateLimiterFilterIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    LoginRateLimitRepository loginRateLimitRepository;


    @AfterEach
    void cleanUp() {
        loginRateLimitRepository.deleteAllInBatch();
    }

}