package com.homelab.authservice.security.authentication.register.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "user-register")
@Getter
@Setter
public class NotificationRegisterEventProperties {
    private String topic;
}
