package com.example;

import com.example.service.TelegramBotService;
import com.example.config.BotConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileWriter;
import java.io.PrintWriter;

@SpringBootApplication
public class BotApplication {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BotApplication.class, args);

        try (PrintWriter log = new PrintWriter(new FileWriter("bot.log", true))) {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            BotConfig config = context.getBean(BotConfig.class);

            TelegramBotService botService;

            try {
                // 1. Пробуем без прокси
                botService = new TelegramBotService(config);
                botsApi.registerBot(botService);
                log.println("✅ [" + System.currentTimeMillis() + "] Бот запущен напрямую без прокси");
                System.out.println("✅ Бот запущен напрямую без прокси!");
            } catch (Exception directError) {
                log.println("❌ [" + System.currentTimeMillis() + "] Ошибка прямого подключения: " + directError.getMessage());
                System.err.println("❌ Ошибка прямого подключения: " + directError.getMessage());
                System.err.println("➡ Переключаемся на SOCKS5‑прокси...");

                try {
                    // 2. Пробуем через SOCKS5
                    DefaultBotOptions socksOptions = new DefaultBotOptions();
                    socksOptions.setProxyHost("127.0.0.1");
                    socksOptions.setProxyPort(1080);
                    socksOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

                    botService = new TelegramBotService(config, socksOptions);
                    botsApi.registerBot(botService);
                    log.println("✅ [" + System.currentTimeMillis() + "] Бот запущен через SOCKS5‑прокси");
                    System.out.println("✅ Бот запущен через SOCKS5‑прокси!");
                } catch (Exception socksError) {
                    log.println("❌ [" + System.currentTimeMillis() + "] SOCKS5 недоступен: " + socksError.getMessage());
                    System.err.println("❌ SOCKS5 недоступен: " + socksError.getMessage());
                    System.err.println("➡ Переключаемся на HTTP‑прокси...");

                    // 3. Пробуем через HTTP
                    DefaultBotOptions httpOptions = new DefaultBotOptions();
                    httpOptions.setProxyHost("127.0.0.1");
                    httpOptions.setProxyPort(8080); // укажи порт своего HTTP‑прокси
                    httpOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);

                    botService = new TelegramBotService(config, httpOptions);
                    botsApi.registerBot(botService);
                    log.println("✅ [" + System.currentTimeMillis() + "] Бот запущен через HTTP‑прокси");
                    System.out.println("✅ Бот запущен через HTTP‑прокси!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

