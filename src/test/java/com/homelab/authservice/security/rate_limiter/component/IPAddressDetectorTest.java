package com.homelab.authservice.security.rate_limiter.component;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class IPAddressDetectorTest {

    private final IPAddressDetector detector = new IPAddressDetector();

    @Test
    void distinguishesClientsReachingTheSameServer() {
        MockHttpServletRequest first = new MockHttpServletRequest();
        first.setLocalAddr("10.0.0.1");
        first.setRemoteAddr("192.0.2.1");
        MockHttpServletRequest second = new MockHttpServletRequest();
        second.setLocalAddr("10.0.0.1");
        second.setRemoteAddr("192.0.2.2");

        assertThat(detector.detect(first)).isEqualTo("192.0.2.1");
        assertThat(detector.detect(second)).isEqualTo("192.0.2.2");
    }

    @Test
    void ignoresUntrustedForwardingHeaders() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.0.2.1");
        request.addHeader("X-Forwarded-For", "198.51.100.1");
        request.addHeader("Forwarded", "for=198.51.100.1");

        assertThat(detector.detect(request)).isEqualTo("192.0.2.1");
    }
}
