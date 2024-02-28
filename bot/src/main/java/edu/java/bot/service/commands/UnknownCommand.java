package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class UnknownCommand implements Command {
    private Command nextCommand;

    @Override
    public String command() {
        return CommandConstants.UNKNOWN_COMMAND.getCommand();
    }

    @Override
    public String description() {
        return CommandConstants.UNKNOWN_COMMAND.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), CommandConstants.UNKNOWN_COMMAND.getResponse());
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
