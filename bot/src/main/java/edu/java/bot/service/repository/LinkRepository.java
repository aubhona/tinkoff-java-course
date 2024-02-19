package edu.java.bot.service.repository;

import java.util.List;

public interface LinkRepository<T> {
    void addLink(String userId, T link);

    void removeLink(String userId, T link);

    boolean isLinkTracked(String userId, T link);

    List<T> getTrackedLinks(String userId);
}
