package edu.java.bot.service.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.commands.*;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChainUserMessageProcessor implements UserMessageProcessor {
    private final Command chain;

    public ChainUserMessageProcessor() {
        Command startCommand = new StartCommand();
        Command helpCommand = new HelpCommand();
        Command trackCommand = new TrackCommand();
        Command untrackCommand = new UntrackCommand();
        Command listCommand = new ListCommand();
        Command unknownCommand = new UnknownCommand();
        startCommand.setNext(helpCommand);
        helpCommand.setNext(trackCommand);
        trackCommand.setNext(untrackCommand);
        untrackCommand.setNext(listCommand);
        listCommand.setNext(unknownCommand);

        this.chain = startCommand;
    }

    @Override
    public List<? extends Command> commands() {
        ArrayList<Command> commands = new ArrayList<>();
        Command currentCommand = chain;
        while (currentCommand != null && currentCommand.command() != null) {
            commands.add(currentCommand);
            currentCommand = currentCommand.getNext();
        }

        return commands;
    }

    @Override
    public SendMessage process(Update update) {
        return chain.handle(update);
    }
}
