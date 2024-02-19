import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.commands.CommandConstants;
import edu.java.bot.service.commands.StartCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class StartCommandTest {

    @Mock
    private Update mockedUpdate;
    @Mock
    private Message mockedMessage;
    @Mock
    private Chat mockedChat;

    @InjectMocks
    private StartCommand startCommand;

    private final Long chatId = 12345L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockedUpdate.message()).thenReturn(mockedMessage);
        when(mockedMessage.chat()).thenReturn(mockedChat);
        when(mockedChat.id()).thenReturn(chatId);
        when(mockedMessage.text()).thenReturn("/start");
    }

    @Test
    void testHandleCorrectly() {
        SendMessage response = startCommand.handle(mockedUpdate);
        assertEquals(CommandConstants.START_COMMAND.getResponse(), response.getParameters().get("text"));
        assertEquals(chatId.toString(), response.getParameters().get("chat_id").toString());
    }
}
