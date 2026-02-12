package com.example.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class ActionMenu {
    public static SendMessage getMenu(Long chatId, String lang) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(false);

        SendMessage message;

        if (lang.equals("Русский")) {
            markup.setKeyboard(List.of(
                    new KeyboardRow(List.of(
                            new KeyboardButton("Распечатать документ"),
                            new KeyboardButton("Ламинат документа")
                    )),
                    new KeyboardRow(List.of(
                            new KeyboardButton("Страхование автомобиля"),
                            new KeyboardButton("Подключение Сим карты")
                    )),
                    new KeyboardRow(List.of(
                            new KeyboardButton("Ремонт компьютера/принтера")
                    )),
                    new KeyboardRow(List.of(
                            new KeyboardButton("Закончить")
                    ))
            ));
            message = new SendMessage(chatId.toString(), "Выберите услугу:");
        } else {
            markup.setKeyboard(List.of(
                    new KeyboardRow(List.of(
                            new KeyboardButton("Hujjatni chop etish"),
                            new KeyboardButton("Hujjatni laminatlash")
                    )),
                    new KeyboardRow(List.of(
                            new KeyboardButton("Avto sug'urta"),
                            new KeyboardButton("SIM karta ulash")
                    )),
                    new KeyboardRow(List.of(
                            new KeyboardButton("Kompyuter/Printer ta'miri")
                    )),
                    new KeyboardRow(List.of(
                            new KeyboardButton("Tugatish")
                    ))
            ));
            message = new SendMessage(chatId.toString(), "Xizmatni tanlang:");
        }

        message.setReplyMarkup(markup);
        return message;
    }
}
