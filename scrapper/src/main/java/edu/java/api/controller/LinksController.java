package edu.java.api.controller;

import dto.AddLinkRequest;
import dto.LinkResponse;
import dto.ListLinksResponse;
import dto.RemoveLinkRequest;
import edu.java.api.exception.ChatNotRegisteredException;
import edu.java.api.exception.LinkAlreadyTrackedException;
import edu.java.api.exception.LinkNotTrackedException;
import edu.java.api.service.LinksService;
import java.net.URI;
import java.util.AbstractMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinksController {
    private final LinksService service;

    private static final String CHAT_NOT_EXIST_MESSAGE = "The chat doesn't exist";
    private static final String LINK_ALREADY_TRACKED_MESSAGE = "The link is already being tracked or incorrect";
    private static final String LINK_NOT_TRACKED_MESSAGE = "The link is not being tracked or incorrect";

    @GetMapping
    public ResponseEntity<ListLinksResponse> getAllLinks(@RequestHeader("Tg-Chat-Id") Long tgChatId)
        throws ChatNotRegisteredException {
        if (!service.chatExist(tgChatId)) {
            throw new ChatNotRegisteredException(CHAT_NOT_EXIST_MESSAGE);
        }
        ListLinksResponse response = new ListLinksResponse();
        response.setLinks(service.getChatLinks(tgChatId).stream()
            .map(pair -> new LinkResponse(pair.getKey(), pair.getValue().toString())).toList());
        response.setSize(response.getLinks().size());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody AddLinkRequest addLinkRequest
    ) throws ChatNotRegisteredException, LinkAlreadyTrackedException {
        if (!service.chatExist(tgChatId)) {
            throw new ChatNotRegisteredException(CHAT_NOT_EXIST_MESSAGE);
        }
        if (!service.addChatLink(tgChatId, addLinkRequest.getLink())) {
            throw new LinkAlreadyTrackedException(LINK_ALREADY_TRACKED_MESSAGE);
        }
        AbstractMap.SimpleEntry<Long, URI> pair = service.findLink(addLinkRequest.getLink());

        return ResponseEntity.ok(new LinkResponse(pair.getKey(), pair.getValue().toString()));
    }

    @DeleteMapping
    public ResponseEntity<LinkResponse> removeLink(
        @RequestHeader("Tg-Chat-Id") Long tgChatId,
        @RequestBody RemoveLinkRequest removeLinkRequest
    ) throws LinkNotTrackedException, ChatNotRegisteredException {
        if (!service.chatExist(tgChatId)) {
            throw new ChatNotRegisteredException(CHAT_NOT_EXIST_MESSAGE);
        }
        if (!service.removeChatLink(tgChatId, removeLinkRequest.getLink())) {
            throw new LinkNotTrackedException(LINK_NOT_TRACKED_MESSAGE);
        }
        AbstractMap.SimpleEntry<Long, URI> pair = service.findLink(removeLinkRequest.getLink());

        return ResponseEntity.ok(new LinkResponse(pair.getKey(), pair.getValue().toString()));
    }
}
