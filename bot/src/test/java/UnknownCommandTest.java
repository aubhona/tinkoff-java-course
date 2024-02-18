package edu.java.bot.service.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnknownCommandTest {

    private UnknownCommand unknownCommand;
    private Update mockedUpdate;
    private Message mockedMessage;
    private Chat mockedChat;
    private final Long chatId = 12345L;

    @BeforeEach
    public void setUp() {
        unknownCommand = new UnknownCommand();

        mockedUpdate = mock(Update.class);
        mockedMessage = mock(Message.class);
        mockedChat = mock(Chat.class);

        when(mockedUpdate.message()).thenReturn(mockedMessage);
        when(mockedMessage.chat()).thenReturn(mockedChat);
        when(mockedChat.id()).thenReturn(chatId);
    }

    @Test
    public void testHandleReturnsCorrectSendMessage() {
        SendMessage result = unknownCommand.handle(mockedUpdate);

        assertEquals(chatId, result.getParameters().get("chat_id"));
        assertEquals("Команда неизвестна.", result.getParameters().get("text"));
    }
}
