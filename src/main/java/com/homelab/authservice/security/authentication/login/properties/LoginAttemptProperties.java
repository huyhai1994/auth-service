package com.homelab.authservice.security.authentication.login.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("security.login-attempt")
@Getter
@Setter
public class LoginAttemptProperties {
    int failureAttempts;
    Duration lockDuration;
}
