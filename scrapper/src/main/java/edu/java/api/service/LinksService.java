package edu.java.api.service;

import edu.java.api.repository.ChatRepository;
import edu.java.api.repository.LinkRepository;
import java.net.URI;
import java.util.AbstractMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Temporary stub.
@Service
@RequiredArgsConstructor
public class LinksService {
    private final LinkRepository<URI> linkRepository;
    private final ChatRepository chatRepository;

    public List<AbstractMap.SimpleEntry<Long, URI>> getChatLinks(Long chatId) {
        return linkRepository.getTrackedLinks(chatId);
    }

    public boolean addChatLink(Long chatId, String link) {
        try {
            if (linkRepository.isLinkTracked(chatId, URI.create(link))) {
                return false;
            }
            linkRepository.addLink(chatId, URI.create(link));
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean removeChatLink(Long chatId, String link) {
        try {
            if (!linkRepository.isLinkTracked(chatId, URI.create(link))) {
                return false;
            }
            linkRepository.removeLink(chatId, URI.create(link));
            return true;
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    public boolean chatExist(Long chatId) {
        return chatRepository.isChatExists(chatId);
    }

    public AbstractMap.SimpleEntry<Long, URI> findLink(String link) {
        try {
            return linkRepository.findLinkId(URI.create(link));
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
