package com.example.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class MainMenu {
    public static SendMessage getMenu(Long chatId, String lang) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        if (lang.equals("Русский")) {
            row.add(new KeyboardButton("Начать"));
            row.add(new KeyboardButton("Закончить"));
        } else {
            row.add(new KeyboardButton("Boshlash"));
            row.add(new KeyboardButton("Tugatish"));
        }

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        String textMsg = lang.equals("Русский") ? "Выберите действие:" : "Amalni tanlang:";
        SendMessage message = new SendMessage(chatId.toString(), textMsg);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }
}
