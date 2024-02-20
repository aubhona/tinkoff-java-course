import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.service.commands.CommandConstants;
import edu.java.bot.service.commands.UnknownCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnknownCommandTest {

    private UnknownCommand unknownCommand;
    private Update mockedUpdate;

    @BeforeEach
    public void setUp() {
        unknownCommand = new UnknownCommand();

        mockedUpdate = mock(Update.class);
        Message mockedMessage = mock(Message.class);
        Chat mockedChat = mock(Chat.class);

        when(mockedUpdate.message()).thenReturn(mockedMessage);
        when(mockedMessage.chat()).thenReturn(mockedChat);
        when(mockedChat.id()).thenReturn(12345L);
    }

    @Test
    public void testHandleReturnsCorrectSendMessage() {
        SendMessage result = unknownCommand.handle(mockedUpdate);

        assertEquals(12345L, result.getParameters().get("chat_id"));
        assertEquals(CommandConstants.UNKNOWN_COMMAND.getResponse(), result.getParameters().get("text"));
    }
}
