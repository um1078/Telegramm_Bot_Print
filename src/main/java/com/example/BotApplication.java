package com.example;

import com.example.service.TelegramBotService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class BotApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BotApplication.class, args);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            TelegramBotService botService = context.getBean(TelegramBotService.class);
            botsApi.registerBot(botService);
            System.out.println("Бот успешно запущен и слушает обновления!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
