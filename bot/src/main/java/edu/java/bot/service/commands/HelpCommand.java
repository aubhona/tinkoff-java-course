package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommand implements Command {
    private Command nextCommand;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Показать помощь по командам";
    }

    @Override
    public SendMessage handle(Update update) {
        if (!supports(update)) {
            return nextCommand.handle(update);
        }
        String helpMessage = "Список команд:\n/start - Зарегистрировать пользователя\n/help - Показать эту помощь\n" +
            "/track - Начать отслеживание ссылки\n/untrack - Прекратить отслеживание ссылки\n" +
            "/list - Показать список отслеживаемых ссылок";
        return new SendMessage(update.message().chat().id(), helpMessage);
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
