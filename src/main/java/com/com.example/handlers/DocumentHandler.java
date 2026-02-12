package com.example.hendlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public class DocumentHandler {
    public static String handleDocumentOptions(Update update) {
        return "Выберите параметры печати:\n" +
                "1) Цветная печать\n" +
                "2) Черно-белая печать\n" +
                "3) В виде книжки\n" +
                "4) A4 формат\n" +
                "5) Укажите количество копий";
    }
}
