package edu.java.bot.api.service;

import com.pengrad.telegrambot.request.SendMessage;
import dto.LinkUpdateRequest;
import edu.java.bot.service.bot.TelegramBotService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatesService {

    private final TelegramBotService bot;
    private static final String UPDATE_MESSAGE_TEMPLATE = "У вас появилось обновление по ссылке:\n%s\nОписание: %s";

    public List<Long> sendUpdateToChats(LinkUpdateRequest request) {
        return request.getTgChatIds().stream()
            .filter(chatId -> {
                boolean chatExists = bot.checkChatExists(chatId);
                if (chatExists) {
                    sendUpdateMessage(chatId, request);
                }
                return !chatExists;
            })
            .collect(Collectors.toList());
    }

    private void sendUpdateMessage(Long chatId, LinkUpdateRequest request) {
        String messageText = String.format(UPDATE_MESSAGE_TEMPLATE, request.getUrl(), request.getDescription());
        SendMessage message = new SendMessage(chatId, messageText);
        bot.execute(message);
    }
}
