import com.pengrad.telegrambot.model.User;
import edu.java.bot.service.commands.CommandConstants;
import edu.java.bot.service.commands.ListCommand;
import edu.java.bot.service.repository.LinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ListCommandTest {

    @Mock
    private Update mockedUpdate;
    @Mock
    private Message mockedMessage;
    @Mock
    private Chat mockedChat;
    @Mock
    private LinkRepository<URI> linkRepository;
    @Mock
    private User mockedUser;

    @InjectMocks
    private ListCommand listCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockedUpdate.message()).thenReturn(mockedMessage);
        when(mockedMessage.chat()).thenReturn(mockedChat);
        when(mockedChat.id()).thenReturn(12345L);
        when(mockedMessage.text()).thenReturn(CommandConstants.LIST_COMMAND.getCommand());
        when(mockedMessage.from()).thenReturn(mockedUser);
        when(mockedUser.id()).thenReturn(123456789L);
    }

    @Test
    void testProcessListCommandEmptyList() {
        when(linkRepository.getTrackedLinks(anyString())).thenReturn(Collections.emptyList());

        SendMessage response = listCommand.handle(mockedUpdate);

        assertEquals(CommandConstants.LIST_COMMAND.getResponse(), response.getParameters().get("text"));
    }

    @Test
    void testProcessListCommandWithLinks() {
        List<URI> links = List.of(URI.create("https://example.com"), URI.create("https://another-example.com"));
        when(linkRepository.getTrackedLinks(anyString())).thenReturn(links);

        SendMessage response = listCommand.handle(mockedUpdate);

        String expectedResponse = "Список отслеживаемых ссылок:\n• https://example.com\n• https://another-example.com\n";
        assertEquals(expectedResponse, response.getParameters().get("text"));
    }
}
