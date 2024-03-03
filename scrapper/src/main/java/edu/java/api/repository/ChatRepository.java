package edu.java.api.repository;

public interface ChatRepository {
    void addChat(Long chatId);

    void removeChat(Long chatId);

    boolean isChatExists(Long chatId);
}
