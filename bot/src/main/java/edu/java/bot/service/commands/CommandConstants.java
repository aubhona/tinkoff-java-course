package edu.java.bot.service.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandConstants {
    HELP_COMMAND("/help", "Получить список доступных команд.",
            """
                    Список команд:
                    /start - Зарегистрировать пользователя
                    /help - Показать эту помощь
                    /track - Начать отслеживание ссылки
                    /untrack - Прекратить отслеживание ссылки
                    /list - Показать список отслеживаемых ссылок"""),
    LIST_COMMAND("/list", "Показать список всех активных подписок.", "Список отслеживаемых ссылок пуст."),
    START_COMMAND("/start", "Начать работу с ботом.", "Добро пожаловать в бота!"),
    TRACK_COMMAND("/track", "Добавить новую подписку на обновления.", "Ссылка добавлена в отслеживание."),
    UNKNOWN_COMMAND(null, null, "Команда неизвестна."),
    UNTRACK_COMMAND("/untrack", "Удалить существующую подписку.", "Отслеживание ссылки прекращено.");

    private final String command;
    private final String description;
    private final String response;
}
