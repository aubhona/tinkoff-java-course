package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import edu.java.bot.service.bots.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication @EnableConfigurationProperties(ApplicationConfig.class) public class BotApplication {
    private final Bot telegramBotService;

    @Autowired public BotApplication(Bot telegramBotService) {
        this.telegramBotService = telegramBotService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BotApplication.class, args);
    }
}
