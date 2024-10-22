package com.example.server.auth.handler;

import com.example.server.auth.utils.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Slf4j
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();  // Tái sử dụng ObjectMapper.

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException ae) throws IOException {

        log.error("Login failed: {}", ae.getMessage());  // Log lỗi để hỗ trợ debug.
        sendErrorResponse(response);
    }

    private void sendErrorResponse(HttpServletResponse response) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));  // Chuyển đổi sang JSON bằng Jackson.
    }
}
