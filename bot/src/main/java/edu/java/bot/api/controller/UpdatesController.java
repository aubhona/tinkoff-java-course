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
    private static final String CHATS_NOT_EXIST_MESSAGE_TEMPLATE = "These chats don't exist: %s";
    private static final String SUCCESSFULLY_PROCESSED_MESSAGE = "Successfully processed";
    private static final String OK_STATUS_CODE = HttpStatus.OK.toString();

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
                String.format(CHATS_NOT_EXIST_MESSAGE_TEMPLATE,
                    String.join(", ", nonExistentChats.stream().map(Object::toString).toList()))
            );
        }
        response.setDescription(SUCCESSFULLY_PROCESSED_MESSAGE);
        response.setCode(OK_STATUS_CODE);

        return ResponseEntity.ok().body(response);
    }
}
