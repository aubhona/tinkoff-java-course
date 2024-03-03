package edu.java.api.repository;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Repository;

// Temporary stub.
@Repository
public class MemoryChatRepository implements ChatRepository {
    private final Set<Long> chatIds = new HashSet<>();

    @SuppressWarnings("MagicNumber")
    public MemoryChatRepository() {
        addChat(1L);
        addChat(2L);
        addChat(3L);
    }

    @Override
    public void addChat(Long chatId) {
        chatIds.add(chatId);
    }

    @Override
    public void removeChat(Long chatId) {
        chatIds.remove(chatId);
    }

    @Override
    public boolean isChatExists(Long chatId) {
        return chatIds.contains(chatId);
    }
}
