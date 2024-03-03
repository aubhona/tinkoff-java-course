package edu.java.bot.api.controller;

import dto.ApiResponse;
import dto.LinkUpdateRequest;
import edu.java.bot.api.exception.ChatNotFoundException;
import edu.java.bot.api.service.UpdatesService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class UpdatesController {

    private final UpdatesService service;

    @PostMapping
    public ResponseEntity<ApiResponse> sendUpdate(
        @RequestBody
        @NonNull
        @Valid
        LinkUpdateRequest request
    ) throws ChatNotFoundException {
        List<Long> nonExistentChats = service.sendUpdateToChats(request);
        ApiResponse response = new ApiResponse();
        if (!nonExistentChats.isEmpty()) {
            throw new ChatNotFoundException(
                "These chats don't exist: "
                    + String.join(", ", nonExistentChats.stream().map(Object::toString).toList())
            );
        }
        response.setDescription("Successfully processed");
        response.setCode(HttpStatus.OK.toString());

        return ResponseEntity.ok().body(response);
    }
}
