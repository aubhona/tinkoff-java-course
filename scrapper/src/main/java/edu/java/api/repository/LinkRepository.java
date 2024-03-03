package edu.java.api.repository;

import java.util.AbstractMap;
import java.util.List;

public interface LinkRepository<T> {
    void addLink(Long chatId, T link);

    void removeLink(Long chatId, T link);

    boolean isLinkTracked(Long chatId, T link);

    List<AbstractMap.SimpleEntry<Long, T>> getTrackedLinks(Long chatId);

    AbstractMap.SimpleEntry<Long, T> findLinkId(T link);
}
