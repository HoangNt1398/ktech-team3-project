package com.example.server.auth.handler;

import com.example.server.auth.utils.ErrorResponder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ae) throws IOException, ServletException {
        Exception exception = (Exception) request.getAttribute("exception");
        ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);
        logExceptionMessage(ae, exception);
    }


    private void logExceptionMessage(AuthenticationException ae, Exception e) {
        String message = e != null ? e.getMessage() : ae.getMessage();
        log.warn("Unauthorized Error 미인증 사용자의 보호엔드포인트 접근시도 : {}", message);
    }
}