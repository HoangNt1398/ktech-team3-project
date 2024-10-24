package com.example.server.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

public class ErrorResponder {

    private static final ObjectMapper objectMapper = new ObjectMapper(); // Sử dụng Jackson

    // HTTP 응답 에러처리
    public static void sendErrorResponse(HttpServletResponse response, HttpStatus httpStatus) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());

        // Chuyển đổi đối tượng sang JSON và gửi qua response
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }
}
