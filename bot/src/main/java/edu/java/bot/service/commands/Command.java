package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.Objects;

public interface Command {
    String command();

    String description();

    SendMessage handle(Update update);

    default boolean supports(Update update) {
        return Objects.equals(update.message().text(), command());
    }

    Command getNext();

    void setNext(Command nextCommand);

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
