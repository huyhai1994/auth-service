package com.homelab.authservice.security.jwt.components;

import com.homelab.authservice.security.jwt.error_code.ErrorCode;
import com.homelab.authservice.shared.response.ApiError;
import com.homelab.authservice.shared.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiError error = new ApiError(
                ErrorCode.UNAUTHORIZED.name(),
                ErrorCode.UNAUTHORIZED.getDefaultMessage()
        );


        objectMapper.writeValue(
                response.getOutputStream(),
                ApiResponse.failure(error )
        );


    }
}
