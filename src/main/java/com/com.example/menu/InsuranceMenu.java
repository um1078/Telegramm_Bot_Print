package menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class InsuranceMenu {

    // Главное меню страхования
    public static SendMessage getMenu(Long chatId, String lang) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        if (lang.equals("Русский")) {
            row.add(new KeyboardButton("Личное авто (Физлицо)"));
            row.add(new KeyboardButton("Юр. лицо"));
        } else {
            row.add(new KeyboardButton("Shaxsiy avtomobil"));
            row.add(new KeyboardButton("Yuridik shaxs"));
        }

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        String text = lang.equals("Русский") ?
                "Выберите тип страхования:" :
                "Sug‘urta turini tanlang:";
        SendMessage message = new SendMessage(chatId.toString(), text);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    // Меню для физического лица
    public static SendMessage getPhysicalMenu(Long chatId, String lang) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        if (lang.equals("Русский")) {
            row.add(new KeyboardButton("До 5 водителей"));
            row.add(new KeyboardButton("Без ограничений"));
        } else {
            row.add(new KeyboardButton("5 ta haydovchigacha"));
            row.add(new KeyboardButton("Cheklanmagan"));
        }

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        String text = lang.equals("Русский") ?
                "Выберите вариант страхования:" :
                "Sug‘urta variantini tanlang:";
        SendMessage message = new SendMessage(chatId.toString(), text);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    // Меню для юридического лица
    public static SendMessage getLegalMenu(Long chatId, String lang) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        if (lang.equals("Русский")) {
            row.add(new KeyboardButton("До 5 водителей (280 000)"));
            row.add(new KeyboardButton("Без ограничений"));
        } else {
            row.add(new KeyboardButton("5 ta haydovchigacha (280 000)"));
            row.add(new KeyboardButton("Cheklanmagan"));
        }

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        String text = lang.equals("Русский") ?
                "Выберите вариант страхования для юр. лица:" :
                "Yuridik shaxs uchun sug‘urta variantini tanlang:";
        SendMessage message = new SendMessage(chatId.toString(), text);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }
}
