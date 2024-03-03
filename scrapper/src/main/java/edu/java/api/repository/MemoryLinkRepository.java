package edu.java.api.repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import org.springframework.stereotype.Repository;

// Temporary stub.
@Repository
public class MemoryLinkRepository implements LinkRepository<URI> {
    private final HashMap<Long, HashSet<Long>> chatJoinLinks = new HashMap<>();
    private final List<URI> links = new ArrayList<>();

    public MemoryLinkRepository() {

        try {
            addLink(1L, new URI("https://example.com"));
            addLink(1L, new URI("https://example.org"));
            addLink(2L, new URI("https://example.net"));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Ошибка в URI", e);
        }
    }

    @Override
    public void addLink(Long chatId, URI link) {
        long id = links.indexOf(link);
        if (id == -1) {
            links.add(link);
            id = links.size() - 1;
        }
        chatJoinLinks.computeIfAbsent(chatId, k -> new HashSet<>()).add(id);
    }

    @Override
    public void removeLink(Long chatId, URI link) {
        long id = links.indexOf(link);
        if (chatJoinLinks.get(chatId) == null || id == -1) {
            return;
        }

        chatJoinLinks.get(chatId).remove(id);
    }

    @Override
    public boolean isLinkTracked(Long chatId, URI link) {
        long id = links.indexOf(link);
        if (chatJoinLinks.get(chatId) == null || id == -1) {
            return false;
        }

        return chatJoinLinks.get(chatId).contains(id);
    }

    @Override
    public List<AbstractMap.SimpleEntry<Long, URI>> getTrackedLinks(Long chatId) {
        List<AbstractMap.SimpleEntry<Long, URI>> trackedLinks = new ArrayList<>();
        HashSet<Long> linkIds = chatJoinLinks.get(chatId);

        if (linkIds != null) {
            for (Long id : linkIds) {
                URI link = links.get(Math.toIntExact(id));
                trackedLinks.add(new AbstractMap.SimpleEntry<>(id, link));
            }
        }

        return trackedLinks;
    }

    @Override
    public AbstractMap.SimpleEntry<Long, URI> findLinkId(URI link) {
        return new AbstractMap.SimpleEntry<>((long) links.indexOf(link), link);
    }

}
