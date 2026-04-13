package com.example.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
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
    public static SendMessage getFormatStep(Long chatId, String lang) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(new KeyboardButton("В виде книжки"));
        row.add(new KeyboardButton("A4 ФОРМАТА"));
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        SendMessage message = new SendMessage(chatId.toString(),
                "O‘zbekcha".equals(lang)
                        ? "📄 Formatni tanlang:"
                        : "📄 Выберите формат:");
        message.setReplyMarkup(keyboardMarkup);
        return message;
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
            com.example.menu.StateManager.setState(chatId, "PRINT_CONFIRM2");
            return message;
        }
    }

    // Третий шаг: количество копий
    public static SendMessage getCopiesStep(Long chatId, String lang) {
        String text;
        if ("Русский".equals(lang)) {
            text = "Введите СКОЛЬКО копий каждого файла ЦИФРОЙ:";
        } else if ("O‘zbekcha".equals(lang)) {
            text = "Nusxalar sonini RAKAM bilan kiriting:";
        } else {
            text = "Введите количество копий:";
        }

        // создаём клавиатуру только с кнопкой "Отменить"
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        KeyboardRow row = new KeyboardRow();
        if ("O‘zbekcha".equals(lang)) {
            row.add(new KeyboardButton("Bekor qilish"));
        } else {
            row.add(new KeyboardButton("Отменить"));
        }
        keyboardMarkup.setKeyboard(java.util.Collections.singletonList(row));
        keyboardMarkup.setResizeKeyboard(true);
//        // создаём клавиатуру только с кнопкой "Отменить"
//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        KeyboardRow row = new KeyboardRow();
//        row.add(new KeyboardButton("Отменить")); // или "Bekor qilish" для узбекского
//        keyboardMarkup.setKeyboard(java.util.Collections.singletonList(row));
//        keyboardMarkup.setResizeKeyboard(true);


        SendMessage message = new SendMessage(chatId.toString(), text);
        // 🔥 Убираем старое меню, чтобы осталась только строка ввода
        message.setReplyMarkup(new ReplyKeyboardRemove(true));
        return message;
    }
    public static SendMessage getSendFileMenu(Long chatId, String lang) {
        String text;
        String buttonText;

        if ("Русский".equals(lang)) {
            text = "📎 Для отправки файла нажмите кнопку ниже:";
            buttonText = "Отправка Файла";
        } else if ("O‘zbekcha".equals(lang)) {
            text = "📎 Fayl yuborish uchun tugmani bosing:";
            buttonText = "Failni Junatish"; // 🔥 узбекский вариант
        } else {
            text = "📎 Send your file:";
            buttonText = "Send File";
        }

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        InlineKeyboardButton linkButton = new InlineKeyboardButton();
        linkButton.setText(buttonText);
        linkButton.setUrl("https://t.me/BextarinUTFS");

        rows.add(Collections.singletonList(linkButton));
        inlineKeyboard.setKeyboard(rows);

        SendMessage message = new SendMessage(chatId.toString(), text);
        message.setReplyMarkup(inlineKeyboard);
        return message;
    }


    //linkButton.setText("Faylni Junatish"); // текст кнопки
//    public static SendMessage getCopiesStep(Long chatId, String lang) {
//        String text;
//        if ("Русский".equals(lang)) {
//            text = "Введите количество копий ЦИФРОЙ:";
//        } else if ("O‘zbekcha".equals(lang)) {
//            text = "Nusxalar sonini RAKAM bilan kiriting:";
//        } else {
//            text = "Введите количество копий:";
//        }
//        SendMessage message = new SendMessage(chatId.toString(), text);
//        // 🔥 Убираем меню, чтобы пользователь мог ввести только цифру
//        message.setReplyMarkup(new ReplyKeyboardRemove(true));
//        return message;
//    }

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
