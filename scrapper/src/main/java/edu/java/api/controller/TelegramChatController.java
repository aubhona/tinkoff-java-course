package edu.java.api.controller;

import dto.ApiResponse;
import edu.java.api.exception.ChatAlreadyRegisteredException;
import edu.java.api.exception.ChatNotRegisteredException;
import edu.java.api.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class TelegramChatController {
    private final TelegramChatService service;

    private static final String CHAT_REGISTERED_SUCCESS_MESSAGE = "The chat has been successfully registered";
    private static final String CHAT_ALREADY_EXISTS_ERROR_MESSAGE = "The chat already exists";
    private static final String CHAT_REMOVED_SUCCESS_MESSAGE = "The chat has been successfully removed";
    private static final String CHAT_DOES_NOT_EXIST_ERROR_MESSAGE = "The chat doesn't exist";

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse> registerChat(@PathVariable Long id) throws ChatAlreadyRegisteredException {
        if (!service.addChat(id)) {
            throw new ChatAlreadyRegisteredException(CHAT_ALREADY_EXISTS_ERROR_MESSAGE);
        }
        ApiResponse response = new ApiResponse();
        response.setDescription(CHAT_REGISTERED_SUCCESS_MESSAGE);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteChat(@PathVariable Long id) throws ChatNotRegisteredException {
        if (!service.removeChat(id)) {
            throw new ChatNotRegisteredException(CHAT_DOES_NOT_EXIST_ERROR_MESSAGE);
        }
        ApiResponse response = new ApiResponse();
        response.setDescription(CHAT_REMOVED_SUCCESS_MESSAGE);

        return ResponseEntity.ok(response);
    }
}
