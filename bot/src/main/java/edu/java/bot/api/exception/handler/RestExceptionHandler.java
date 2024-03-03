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

    private static final String ERROR_MESSAGE = "An error has occurred";
    private static final HttpStatus CHAT_NOT_FOUND_STATUS = HttpStatus.BAD_REQUEST;
    private static final HttpStatus GENERAL_ERROR_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    private ApiResponse getExceptionResponse(Exception exception, HttpStatus httpStatus) {
        ApiResponse response = new ApiResponse();
        response.setDescription(ERROR_MESSAGE);
        response.setExceptionMessage(exception.getMessage());
        response.setCode(httpStatus.toString());
        response.setStacktrace(Arrays.stream(exception.getStackTrace())
            .map(StackTraceElement::toString)
            .toList());
        response.setExceptionName(exception.getClass().getName());
        return response;
    }

    @ExceptionHandler(ChatNotFoundException.class)
    public ResponseEntity<ApiResponse> handleChatException(Exception exception) {
        return new ResponseEntity<>(
            getExceptionResponse(exception, CHAT_NOT_FOUND_STATUS),
            CHAT_NOT_FOUND_STATUS
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception exception) {
        return new ResponseEntity<>(
            getExceptionResponse(exception, GENERAL_ERROR_STATUS),
            GENERAL_ERROR_STATUS
        );
    }
}
