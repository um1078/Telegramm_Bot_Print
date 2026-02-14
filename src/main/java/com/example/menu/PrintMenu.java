package com.example.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class PrintMenu {

    // Первый шаг: выбор цветной или ч/б печати
    public static SendMessage getFirstStep(Long chatId, String lang) {
        //String lang = StateManager.getLang(chatId);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);

        if (lang.equals("Русский")) {
            markup.setKeyboard(List.of(
                    new KeyboardRow(List.of(new KeyboardButton("Цветная печать"), new KeyboardButton("Черно-белая печать")))
            ));
            SendMessage message = new SendMessage(chatId.toString(), "Выберите тип печати:");
            message.setReplyMarkup(markup);
            StateManager.setState(chatId, "PRINT_CONFIRM");
            return message;
        } else {
            markup.setKeyboard(List.of(
                    new KeyboardRow(List.of(new KeyboardButton("Rangli chop etish"), new KeyboardButton("Qora-oq chop etish")))
            ));
            SendMessage message = new SendMessage(chatId.toString(), "Chop etish turini tanlang:");
            message.setReplyMarkup(markup);
            com.example.menu.StateManager.setState(chatId, "PRINT_CONFIRM");
            return message;
        }
    }

    // Меню подтверждения
    public static SendMessage confirmStep(Long chatId, String lang) {
        //String lang = StateManager.getLang(chatId);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);

        if (lang.equals("Русский")) {
            markup.setKeyboard(List.of(
                    new KeyboardRow(List.of(new KeyboardButton("Подтвердить"), new KeyboardButton("Отменить")))
            ));
            SendMessage message = new SendMessage(chatId.toString(), "Подтвердите или отмените выбор:");
            message.setReplyMarkup(markup);
            return message;
        } else {
            markup.setKeyboard(List.of(
                    new KeyboardRow(List.of(new KeyboardButton("Tasdiqlash"), new KeyboardButton("Bekor qilish")))
            ));
            SendMessage message = new SendMessage(chatId.toString(), "Tanlovni tasdiqlang yoki bekor qiling:");
            message.setReplyMarkup(markup);
            return message;
        }
    }

    // Второй шаг: книжка / A4
    public static SendMessage getSecondStep(Long chatId, String lang) {
        //String lang = StateManager.getLang(chatId);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);

        if (lang.equals("Русский")) {
            markup.setKeyboard(List.of(
                    new KeyboardRow(List.of(new KeyboardButton("В виде книжки"), new KeyboardButton("A4 ФОРМАТА")))
            ));
            SendMessage message = new SendMessage(chatId.toString(), "Выберите формат:");
            message.setReplyMarkup(markup);
            StateManager.setState(chatId, "PRINT_CONFIRM2");
            return message;
        } else {
            markup.setKeyboard(List.of(
                    new KeyboardRow(List.of(new KeyboardButton("Kitob shaklida"), new KeyboardButton("A4 FORMATI")))
            ));
            SendMessage message = new SendMessage(chatId.toString(), "Formatni tanlang:");
            message.setReplyMarkup(markup);
            StateManager.setState(chatId, "PRINT_CONFIRM2");
            return message;
        }
    }

    // Третий шаг: количество копий
    public static SendMessage getCopiesStep(Long chatId, String lang) {
        String text;
        if ("Русский".equals(lang)) {
            text = "Введите количество копий ЦИФРОЙ:";
        } else if ("O‘zbekcha".equals(lang)) {
            text = "Nusxalar sonini RAKAM bilan kiriting:";
        } else {
            text = "Введите количество копий:";
        }
        return new SendMessage(chatId.toString(), text);
    }

    // Финальный шаг: показать все ответы
    public static SendMessage getSummary(Long chatId, String lang) {
        String summary;
        if ("Русский".equals(lang)) {
            summary = "✅ Ваши параметры печати:\n" +
                    com.example.menu.StateManager.getDoc(chatId) +
                    "\n📎 Ваш файл отправлен на обработку Админу.\nСпасибо за использование сервиса!";
        } else if ("O‘zbekcha".equals(lang)) {
            summary = "✅ Sizning chop etish parametrlari:\n" +
                    com.example.menu.StateManager.getDoc(chatId) +
                    "\n📎 Faylingiz Administratorga ko‘rib chiqish uchun yuborildi.\nXizmatimizdan foydalanganingiz uchun rahmat!";
        } else {
            summary = "✅ Ваши параметры печати:\n" +
                    com.example.menu.StateManager.getDoc(chatId) +
                    "\n📎 Ваш файл отправлен на обработку Админу.\nСпасибо за использование сервиса!";
        }

        // Добавляем клавиатуру с кнопками
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setResizeKeyboard(true);

        if ("Русский".equals(lang)) {
            markup.setKeyboard(List.of(
                    new KeyboardRow(List.of(new KeyboardButton("Закончить"), new KeyboardButton("Старт")))
            ));
        } else if ("O‘zbekcha".equals(lang)) {
            markup.setKeyboard(List.of(
                    new KeyboardRow(List.of(new KeyboardButton("Tugatish"), new KeyboardButton("Boshlash")))
            ));
        }

        SendMessage message = new SendMessage(chatId.toString(), summary);
        message.setReplyMarkup(markup);
        return message;
    }
}
