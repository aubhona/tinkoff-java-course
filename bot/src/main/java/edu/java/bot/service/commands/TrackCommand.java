package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class TrackCommand implements Command {
    private Command nextCommand;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Начать отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        if (!supports(update)) {
            return nextCommand.handle(update);
        }
        String messageText = "Ссылка добавлена в отслеживание. (Функционал в разработке)";
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
