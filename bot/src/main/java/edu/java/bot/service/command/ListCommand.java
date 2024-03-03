package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import edu.java.bot.service.repository.LinkRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ListCommand implements Command {
    private Command nextCommand;
    private final static String LINK_HEADER_MESSAGE = "Список отслеживаемых ссылок:\n";
    private final LinkRepository<URI> linkRepository;

    @Override
    public String command() {
        return CommandConstants.LIST_COMMAND.getCommand();
    }

    @Override
    public String description() {
        return CommandConstants.LIST_COMMAND.getDescription();
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

        List<URI> links = linkRepository.getTrackedLinks(update.message().from().id().toString());

        if (links.isEmpty()) {
            return new SendMessage(update.message().chat().id(), CommandConstants.LIST_COMMAND.getResponse());
        }

        StringBuilder messageText = new StringBuilder(LINK_HEADER_MESSAGE);
        for (URI link : links) {
            messageText.append("• ").append(link.toString()).append("\n");
        }

        return new SendMessage(update.message().chat().id(), messageText.toString()).parseMode(ParseMode.Markdown);
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
