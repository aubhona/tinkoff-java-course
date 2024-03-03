package edu.java.api.service;

import edu.java.api.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Temporary stub.
@Service
@RequiredArgsConstructor
public class TelegramChatService {
    private final ChatRepository chatRepository;

    public boolean addChat(Long chatId) {
        if (chatRepository.isChatExists(chatId)) {
            return false;
        }

        chatRepository.addChat(chatId);
        return true;
    }

    public boolean removeChat(Long chatId) {
        if (chatRepository.isChatExists(chatId)) {
            return false;
        }

        chatRepository.removeChat(chatId);
        return true;
    }
}
