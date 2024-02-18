package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class ListCommand implements Command {
    private Command nextCommand;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "Показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        if (!supports(update)) {
            return nextCommand.handle(update);
        }
        String messageText = "Список отслеживаемых ссылок пуст. (Функционал в разработке)";
        return new SendMessage(update.message().chat().id(), messageText);
    }

    @Override
    public Command getNext() {
        return nextCommand;
    }

    @Override
    public void setNext(Command nextCommand) {
        this.nextCommand = nextCommand;
    }
}
