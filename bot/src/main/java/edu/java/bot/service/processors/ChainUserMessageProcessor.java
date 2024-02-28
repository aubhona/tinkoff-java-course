package edu.java.bot.service.processors;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.commands.Command;
import edu.java.bot.service.commands.HelpCommand;
import edu.java.bot.service.commands.ListCommand;
import edu.java.bot.service.commands.StartCommand;
import edu.java.bot.service.commands.TrackCommand;
import edu.java.bot.service.commands.UnknownCommand;
import edu.java.bot.service.commands.UntrackCommand;
import edu.java.bot.service.parsers.LinkConstants;
import edu.java.bot.service.parsers.LinkParser;
import edu.java.bot.service.repository.LinkRepository;
import edu.java.bot.service.repository.MemoryLinkRepository;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ChainUserMessageProcessor implements UserMessageProcessor {
    private final Command chain;

    public ChainUserMessageProcessor() {
        Command startCommand = new StartCommand();
        Command helpCommand = new HelpCommand();
        // Temporary stub.
        LinkRepository<URI> repository = new MemoryLinkRepository();
        Command trackCommand = new TrackCommand(new LinkParser(LinkConstants.SUPPORTED_LINKS.getSupportedLinks()),
            repository);
        Command untrackCommand = new UntrackCommand(repository);
        Command listCommand = new ListCommand(repository);
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
