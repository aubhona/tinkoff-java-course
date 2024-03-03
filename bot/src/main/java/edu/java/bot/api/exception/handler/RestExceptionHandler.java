package edu.java.bot.api.exception.handler;

import dto.ApiResponse;
import edu.java.bot.api.exception.ChatNotFoundException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    private ApiResponse getExceptionResponse(Exception exception, HttpStatus httpStatus) {
        ApiResponse response = new ApiResponse();
        response.setDescription("An error has occurred");
        response.setExceptionMessage(exception.getMessage());
        response.setCode(httpStatus.toString());
        response.setStacktrace(Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList());
        response.setExceptionName(exception.getClass().getName());
        return response;
    }

    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<ApiResponse> handleChatException(Exception exception) {
        return new ResponseEntity<>(
            getExceptionResponse(exception, HttpStatus.BAD_REQUEST),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception exception) {
        return new ResponseEntity<>(
            getExceptionResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
