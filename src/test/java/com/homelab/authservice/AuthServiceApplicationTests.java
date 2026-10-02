package com.homelab.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import support.AbstractIntegrationTest;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceApplicationTests extends AbstractIntegrationTest {

    @Test
    void contextLoads() {
    }

}
