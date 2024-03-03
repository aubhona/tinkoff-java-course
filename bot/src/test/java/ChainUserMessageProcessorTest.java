import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.command.CommandConstants;
import edu.java.bot.service.parser.LinkParser;
import edu.java.bot.service.processor.ChainUserMessageProcessor;
import java.net.URI;
import java.util.AbstractMap;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class ChainUserMessageProcessorTest {

    @Mock
    private Update mockedUpdate;
    @Mock
    private Message mockedMessage;
    @Mock
    private Chat mockedChat;
//    @Mock
//    private LinkRepository<URI> linkRepository;
    @Mock
    private User mockedUser;
    private MockedStatic<LinkParser> mockedLinkParser;

    @InjectMocks
    private ChainUserMessageProcessor processor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockedUpdate.message()).thenReturn(mockedMessage);
        when(mockedMessage.chat()).thenReturn(mockedChat);
        when(mockedChat.id()).thenReturn(12345L);
        when(mockedMessage.from()).thenReturn(mockedUser);
        when(mockedUser.id()).thenReturn(123456789L);
        mockedLinkParser = Mockito.mockStatic(LinkParser.class);
    }

    @AfterEach
    void tearDown() {
        mockedLinkParser.close(); // Освобождаем ресурсы MockedStatic
    }

    @Test
    void testProcessUnknownCommand() {
        when(mockedMessage.text()).thenReturn("/unknown");
        SendMessage response = processor.process(mockedUpdate);
        Assertions.assertTrue(response.getParameters().get("text").toString().contains(CommandConstants.UNKNOWN_COMMAND.getResponse()));
    }

    @Test
    void testProcessStartCommand() {
        when(mockedMessage.text()).thenReturn(CommandConstants.START_COMMAND.getCommand());
        SendMessage response = processor.process(mockedUpdate);
        Assertions.assertTrue(response.getParameters().get("text").toString().contains(CommandConstants.START_COMMAND.getResponse()));
    }

    @Test
    void testProcessHelpCommand() {
        when(mockedMessage.text()).thenReturn(CommandConstants.HELP_COMMAND.getCommand());
        SendMessage response = processor.process(mockedUpdate);
        Assertions.assertTrue(response.getParameters().get("text").toString().contains(CommandConstants.HELP_COMMAND.getResponse()));
    }

    @Test
    void testProcessTrackCommandAndInputLink() {
        when(mockedMessage.text()).thenReturn(CommandConstants.TRACK_COMMAND.getCommand());
        processor.process(mockedUpdate);

        when(mockedMessage.text()).thenReturn("https://stackoverflow.com/questions/75908405/getting-around-the-deprecation-of-java-net-url");
        mockedLinkParser.when(() -> LinkParser.tryParse("https://stackoverflow.com/questions/75908405/getting-around-the-deprecation-of-java-net-url"))
            .thenReturn(new AbstractMap.SimpleEntry<>(true, URI.create("https://stackoverflow.com/questions/75908405/getting-around-the-deprecation-of-java-net-url")));

        SendMessage response = processor.process(mockedUpdate);
        Assertions.assertTrue(response.getParameters().get("text").toString().contains(CommandConstants.TRACK_COMMAND.getResponse()));
    }

    @Test
    void testProcessListCommandEmptyList() {
        when(mockedMessage.text()).thenReturn(CommandConstants.LIST_COMMAND.getCommand());
        //when(linkRepository.getTrackedLinks(anyString())).thenReturn(new ArrayList<>());

        SendMessage response = processor.process(mockedUpdate);
        Assertions.assertTrue(response.getParameters().get("text").toString().contains(CommandConstants.LIST_COMMAND.getResponse()));
    }

    @Test
    void testProcessUntrackCommandAndInputLink() {
        when(mockedMessage.text()).thenReturn(CommandConstants.UNTRACK_COMMAND.getCommand());
        processor.process(mockedUpdate);

        when(mockedMessage.text()).thenReturn("https://stackoverflow.com/questions/75908405/getting-around-the-deprecation-of-java-net-url");
        mockedLinkParser.when(() -> LinkParser.tryParse("https://stackoverflow.com/questions/75908405/getting-around-the-deprecation-of-java-net-url"))
            .thenReturn(new AbstractMap.SimpleEntry<>(true, URI.create("https://stackoverflow.com/questions/75908405/getting-around-the-deprecation-of-java-net-url")));

        SendMessage response = processor.process(mockedUpdate);
        Assertions.assertTrue(response.getParameters().get("text").toString().contains(CommandConstants.UNTRACK_COMMAND.getResponse()));
    }
}
