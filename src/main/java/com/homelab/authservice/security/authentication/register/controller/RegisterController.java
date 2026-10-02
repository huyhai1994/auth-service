package com.homelab.authservice.security.authentication.register.controller;

import com.homelab.authservice.security.authentication.register.dto.RegisterRequest;
import com.homelab.authservice.security.authentication.register.dto.RegisterResponse;
import com.homelab.authservice.security.authentication.register.service.UserAccountRegisterService;
import com.homelab.authservice.shared.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class RegisterController {

    private final UserAccountRegisterService userAccountRegisterService;

    @PostMapping("register")
    ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid
            @RequestBody
            RegisterRequest request) {
        RegisterResponse response = userAccountRegisterService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

}
