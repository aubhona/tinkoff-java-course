package edu.java.bot.service.repository;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MemoryLinkRepository implements LinkRepository<URI> {
    private final HashMap<String, HashSet<URI>> links = new HashMap<>();

    @Override
    public void addLink(String userId, URI link) {
        links.computeIfAbsent(userId, k -> new HashSet<>()).add(link);
    }

    @Override
    public void removeLink(String userId, URI link) {
        if (links.get(userId) == null) {
            return;
        }

        links.get(userId).remove(link);
    }

    @Override
    public boolean isLinkTracked(String userId, URI link) {
        if (links.get(userId) == null) {
            return false;
        }

        return links.get(userId).contains(link);
    }

    @Override
    public List<URI> getTrackedLinks(String userId) {
        HashSet<URI> userLinks = links.getOrDefault(userId, new HashSet<>());
        return new ArrayList<>(userLinks);
    }
}
