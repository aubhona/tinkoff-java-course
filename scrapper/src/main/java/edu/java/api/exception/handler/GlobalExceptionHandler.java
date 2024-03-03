package edu.java.api.exception.handler;

import dto.ApiResponse;
import edu.java.api.exception.ChatAlreadyRegisteredException;
import edu.java.api.exception.ChatNotRegisteredException;
import edu.java.api.exception.LinkAlreadyTrackedException;
import edu.java.api.exception.LinkNotTrackedException;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String ERROR_MESSAGE = "An error has occurred";

    private ApiResponse getExceptionResponse(Exception exception, HttpStatus httpStatus) {
        ApiResponse response = new ApiResponse();
        response.setDescription(ERROR_MESSAGE);
        response.setExceptionMessage(exception.getMessage());
        response.setCode(httpStatus.toString());
        response.setStacktrace(Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList());
        response.setExceptionName(exception.getClass().getName());
        return response;
    }

    @ExceptionHandler(ChatAlreadyRegisteredException.class)
    public ResponseEntity<ApiResponse> handleChatExistException(Exception exception) {
        return new ResponseEntity<>(
            getExceptionResponse(exception, HttpStatus.BAD_REQUEST),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ChatNotRegisteredException.class)
    public ResponseEntity<ApiResponse> handleChatNotExistException(Exception exception) {
        return new ResponseEntity<>(
            getExceptionResponse(exception, HttpStatus.NOT_FOUND),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(LinkAlreadyTrackedException.class)
    public ResponseEntity<ApiResponse> handleLinkExistException(Exception exception) {
        return new ResponseEntity<>(
            getExceptionResponse(exception, HttpStatus.BAD_REQUEST),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(LinkNotTrackedException.class)
    public ResponseEntity<ApiResponse> handleLinkNotExistException(Exception exception) {
        return new ResponseEntity<>(
            getExceptionResponse(exception, HttpStatus.NOT_FOUND),
            HttpStatus.NOT_FOUND
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
