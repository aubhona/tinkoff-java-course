package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class HelpCommand implements Command {
    private Command nextCommand;

    @Override
    public String command() {
        return CommandConstants.HELP_COMMAND.getCommand();
    }

    @Override
    public String description() {
        return CommandConstants.HELP_COMMAND.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        if (!supports(update)) {
            return nextCommand.handle(update);
        }
        if (nextCommand != null) {
            nextCommand.handle(update);
        }

        return new SendMessage(update.message().chat().id(), CommandConstants.HELP_COMMAND.getResponse());
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
