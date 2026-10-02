package com.homelab.authservice.shared.aspect;

import com.homelab.authservice.security.authentication.login.exception.UserAccountLockedException;
import com.homelab.authservice.security.authentication.register.exception.PasswordLengthExceededException;
import com.homelab.authservice.security.authentication.register.exception.PasswordTooShortException;
import com.homelab.authservice.security.authentication.register.exception.UsernameAlreadyExistsException;
import com.homelab.authservice.security.authentication.register.exception.UsernameLengthExceededException;
import com.homelab.authservice.security.rate_limiter.exceptions.LoginRateLimitExceededException;
import com.homelab.authservice.shared.error_code.ErrorCode;
import com.homelab.authservice.shared.response.ApiError;
import com.homelab.authservice.shared.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    public ResponseEntity<ApiResponse<Void>> handleInternalServerError(
            Exception exception
    ) {
        log.error(
                "Internal server error: exceptionType={}, message={}",
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                exception
        );

        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ApiResponse<Void>> buildErrorResponse(
            HttpStatus status,
            ErrorCode errorCode
    ) {
        ApiError error = new ApiError(
                errorCode.name(),
                errorCode.getDefaultMessage()
        );

        return ResponseEntity
                .status(status)
                .body(ApiResponse.failure(error));
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameAlreadyExists(
            UsernameAlreadyExistsException exception
    ) {
        return buildErrorResponse(
                HttpStatus.CONFLICT,
                ErrorCode.USERNAME_ALREADY_EXISTS
        );
    }

    @ExceptionHandler(UsernameLengthExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameLengthExceeded(
            UsernameLengthExceededException exception
    ) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCode.USERNAME_LENGTH_EXCEEDED
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCode.VALIDATION_ERROR
        );
    }

    @ExceptionHandler(PasswordTooShortException.class)
    public ResponseEntity<ApiResponse<Void>> handlePasswordTooShort(
            PasswordTooShortException exception
    ) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCode.PASSWORD_TOO_SHORT
        );
    }

    @ExceptionHandler(PasswordLengthExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handlePasswordLengthExceeded(
            PasswordLengthExceededException exception
    ) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                ErrorCode.PASSWORD_LENGTH_EXCEEDED
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(
            UsernameNotFoundException exception
    ) {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.INVALID_CREDENTIALS
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(
            BadCredentialsException exception
    ) {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                ErrorCode.INVALID_CREDENTIALS
        );
    }

    @ExceptionHandler(LoginRateLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleRateLimitExceeded(
            LoginRateLimitExceededException exception
    ) {
        return buildErrorResponse(
                HttpStatus.TOO_MANY_REQUESTS,
                ErrorCode.TOO_MANY_REQUESTS
        );
    }

    @ExceptionHandler(UserAccountLockedException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserAccountLocked(
            UserAccountLockedException exception
    ) {
        return buildErrorResponse(
                HttpStatus.LOCKED,
                ErrorCode.USER_ACCOUNT_LOCKED
        );
    }
}