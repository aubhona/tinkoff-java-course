package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.repository.LinkRepository;
import lombok.RequiredArgsConstructor;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
public class ListCommand implements Command {
    private Command nextCommand;
    private final LinkRepository<URI> linkRepository;
    private final static String LINK_HEADER_MESSAGE = "<b>Список отслеживаемых ссылок:</b>\n<ul>";

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
            return nextCommand.handle(update);
        }
        nextCommand.handle(update);

        List<URI> links = linkRepository.getTrackedLinks(update.message().from().id().toString());

        if (links.isEmpty()) {
            return new SendMessage(update.message().chat().id(), CommandConstants.HELP_COMMAND.getResponse());
        }

        StringBuilder messageText = new StringBuilder(LINK_HEADER_MESSAGE);
        for (URI link : links) {
            messageText.append("<li>").append(link.toString()).append("</li>");
        }
        messageText.append("</ul>");

        return new SendMessage(update.message().chat().id(), messageText.toString()).parseMode(ParseMode.HTML);
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
