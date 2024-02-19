import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.parsers.LinkParser;
import edu.java.bot.service.processors.ChainUserMessageProcessor;
import edu.java.bot.service.repository.LinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.net.URI;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

class ChainUserMessageProcessorTest {

    @Mock
    private Update mockedUpdate;
    @Mock
    private Message mockedMessage;
    @Mock
    private Chat mockedChat;
    @Mock
    private LinkRepository<URI> linkRepository;
    @Mock
    private LinkParser linkParser;

    @InjectMocks
    private ChainUserMessageProcessor processor;

    private final Long chatId = 12345L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockedUpdate.message()).thenReturn(mockedMessage);
        when(mockedMessage.chat()).thenReturn(mockedChat);
        when(mockedChat.id()).thenReturn(chatId);
    }

    @Test
    void testProcessUnknownCommand() {
        when(mockedMessage.text()).thenReturn("/unknown");
        SendMessage response = processor.process(mockedUpdate);
        assertTrue(response.getParameters().get("text").toString().contains("Команда неизвестна."));
    }

}
