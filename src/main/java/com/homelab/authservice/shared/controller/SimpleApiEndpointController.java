package com.homelab.authservice.shared.controller;

import com.homelab.authservice.shared.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me")
public class SimpleApiEndpointController {

    @GetMapping
    ResponseEntity<ApiResponse<String>> getUserMe(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(authentication.getName()));
    }
}
