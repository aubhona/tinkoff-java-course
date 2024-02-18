import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.commands.StartCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StartCommandTest {

    private StartCommand startCommand;
    private Update mockedUpdate;
    private Message mockedMessage;
    private Chat mockedChat;
    private final Long chatId = 12345L;

    @BeforeEach
    public void setUp() {
        startCommand = new StartCommand();
        mockedUpdate = mock(Update.class);
        mockedMessage = mock(Message.class);
        mockedChat = mock(Chat.class);

        when(mockedUpdate.message()).thenReturn(mockedMessage);
        when(mockedMessage.chat()).thenReturn(mockedChat);
        when(mockedChat.id()).thenReturn(chatId);
        when(mockedMessage.text()).thenReturn("/start");
    }

    @Test
    public void testHandleCorrectly() {
        SendMessage response = startCommand.handle(mockedUpdate);
        assertEquals("Добро пожаловать в бота!", response.getParameters().get("text"));
        assertEquals(chatId, response.getParameters().get("chat_id"));
    }
}
