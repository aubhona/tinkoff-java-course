package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class StartCommand implements Command {
    private Command nextCommand;

    @Override
    public String command() {
        return CommandConstants.START_COMMAND.getCommand();
    }

    @Override
    public String description() {
        return CommandConstants.START_COMMAND.getDescription();
    }

    @Override
    public SendMessage handle(Update update) {
        if (!supports(update)) {
            if (nextCommand == null) {
                return null;
            }

            return nextCommand.handle(update);
        }
        if (nextCommand != null) {
            nextCommand.handle(update);
        }

        return new SendMessage(update.message().chat().id(), CommandConstants.START_COMMAND.getResponse());
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
