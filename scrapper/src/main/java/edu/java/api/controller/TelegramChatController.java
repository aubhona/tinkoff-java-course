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

    @PostMapping("/{id}")
    public ResponseEntity<ApiResponse> registerChat(@PathVariable Long id) throws ChatAlreadyRegisteredException {
        if (!service.addChat(id)) {
            throw new ChatAlreadyRegisteredException("The chat already exists");
        }
        ApiResponse response = new ApiResponse();
        response.setDescription("The chat has been successfully registered");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteChat(@PathVariable Long id) throws ChatNotRegisteredException {
        if (!service.removeChat(id)) {
            throw new ChatNotRegisteredException("The chat doesn't exist");
        }
        ApiResponse response = new ApiResponse();
        response.setDescription("The chat has been successfully removed");

        return ResponseEntity.ok(response);
    }
}
