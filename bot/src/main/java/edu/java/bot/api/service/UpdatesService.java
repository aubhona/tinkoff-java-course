package edu.java.bot.api.service;

import com.pengrad.telegrambot.request.SendMessage;
import dto.LinkUpdateRequest;
import edu.java.bot.service.bot.TelegramBotService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Temporary stub.
@Service
@RequiredArgsConstructor
public class UpdatesService {

    private final TelegramBotService bot;

    public List<Long> sendUpdateToChats(LinkUpdateRequest request) {
        List<Long> nonExistentChats = new ArrayList<>();
        for (Long chatId : request.getTgChatIds()) {
            if (!bot.checkChatExists(chatId)) {
                nonExistentChats.add(chatId);
                continue;
            }
            SendMessage message = new SendMessage(
                chatId,
                String.format(
                    "У вас появилось обновление по ссылке:\n%s\n%s",
                    request.getDescription(),
                    request.getDescription()
                )
            );

            bot.execute(message);
        }

        return nonExistentChats;
    }
}
