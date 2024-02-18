import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.processors.ChainUserMessageProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChainUserMessageProcessorTest {

    private ChainUserMessageProcessor processor;
    private Update mockedUpdate;
    private Message mockedMessage;
    private Chat mockedChat;
    private final Long chatId = 12345L;

    @BeforeEach
    public void setUp() {
        processor = new ChainUserMessageProcessor();
        mockedUpdate = mock(Update.class);
        mockedMessage = mock(Message.class);
        mockedChat = mock(Chat.class);

        when(mockedUpdate.message()).thenReturn(mockedMessage);
        when(mockedMessage.chat()).thenReturn(mockedChat);
        when(mockedChat.id()).thenReturn(chatId);
    }

    @Test
    public void testProcessUnknownCommand() {
        when(mockedMessage.text()).thenReturn("/unknown");
        SendMessage response = processor.process(mockedUpdate);
        assertTrue(response.getParameters().get("text").toString().contains("Команда неизвестна."));

        when(mockedMessage.text()).thenReturn("/list");
        response = processor.process(mockedUpdate);
        assertTrue(response.getParameters().get("text").toString().contains("Список отслеживаемых ссылок пуст. (Функционал в разработке)"));
    }
}
