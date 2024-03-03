package edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.parser.LinkParser;
import java.net.URI;
import java.util.Map;
import edu.java.bot.service.repository.LinkRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TrackCommand implements Command {
    private final static String INCORRECT_LINK_MESSAGE = "Некорректная ссылка.";
    private final static String UNSUPPORTED_LINK_MESSAGE = "Неподдерживаемая ссылка.";
    private final static String INPUT_LINK_MESSAGE = "Введите ссылку.";
    private Command nextCommand;
    private boolean isExpectLink = false;
    private final LinkParser linkParser;
    private final LinkRepository<URI> linkRepository;

    @Override
    public String command() {
        return CommandConstants.TRACK_COMMAND.getCommand();
    }

    @Override
    public String description() {
        return CommandConstants.TRACK_COMMAND.getDescription();
    }

    @Override
    public boolean supports(Update update) {
        return update.message().text().startsWith(command()) || isExpectLink;
    }

    @Override
    public SendMessage handle(Update update) {
        if (!supports(update) || (isExpectLink && update.message().text().startsWith("/"))) {
            isExpectLink = false;

            return nextCommand != null ? nextCommand.handle(update) : null;
        }
        if (!isExpectLink) {
            isExpectLink = true;
            return new SendMessage(update.message().chat().id(), INPUT_LINK_MESSAGE);
        }
        if (nextCommand != null) {
            nextCommand.handle(update);
        }
        Map.Entry<Boolean, URI> booleanURIEntry = LinkParser.tryParse(update.message().text());
        linkParser.setUri(booleanURIEntry.getValue());

        if (!booleanURIEntry.getKey() || !linkParser.isValidHost()) {
            isExpectLink = false;
            return new SendMessage(update.message().chat().id(), !booleanURIEntry.getKey() ? INCORRECT_LINK_MESSAGE
                : UNSUPPORTED_LINK_MESSAGE);
        }

        linkRepository.addLink(update.message().from().id().toString(), booleanURIEntry.getValue());

        return new SendMessage(update.message().chat().id(), CommandConstants.TRACK_COMMAND.getResponse());
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
