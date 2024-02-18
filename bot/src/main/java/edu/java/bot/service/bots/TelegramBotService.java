package edu.java.bot.service.bots;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.commands.Command;
import edu.java.bot.service.processors.UserMessageProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramBotService implements Bot {
    private final TelegramBot bot;
    private final UserMessageProcessor messageProcessor;
    private final ApplicationConfig config;

    @Autowired
    public TelegramBotService(ApplicationConfig config, UserMessageProcessor messageProcessor) {
        this.config = config;
        this.bot = new TelegramBot(this.config.telegramToken());
        this.messageProcessor = messageProcessor;
        this.bot.setUpdatesListener(this);
        registerCommands();
    }

    private void registerCommands() {
        List<BotCommand> botCommands = new ArrayList<>();
        for (Command command : messageProcessor.commands()) {
            botCommands.add(command.toApiCommand());
        }
        SetMyCommands setMyCommands = new SetMyCommands(botCommands.toArray(new BotCommand[0]));
        BaseResponse response = bot.execute(setMyCommands);
        if (!response.isOk()) {
            System.err.println("Ошибка при регистрации команд: " + response.description());
        }
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        bot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() != null) {
                execute(messageProcessor.process(update));
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void start() {
        bot.setUpdatesListener(this);
    }

    @Override
    public void close() {
        bot.removeGetUpdatesListener();
    }
}
