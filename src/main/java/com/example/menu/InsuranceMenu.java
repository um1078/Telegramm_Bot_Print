package com.example.menu;



import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class InsuranceMenu {

    public static SendMessage getInsuranceTypeMenu(Long chatId, String lang) {
        return getMenu(chatId, lang);
    }


    // Главное меню страхования
    public static SendMessage getMenu(Long chatId, String lang) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        if (lang.equals("Русский")) {
            row.add(new KeyboardButton("Легковое Авто"));
            row.add(new KeyboardButton("Грузовое Авто"));
        } else {
            row.add(new KeyboardButton("Engil Avto"));
            row.add(new KeyboardButton("Yuk Avto"));
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
            row.add(new KeyboardButton("До 5-ти водителей = 160 000 сум"));
            row.add(new KeyboardButton("Без ограничений = 320 000 сум"));
        } else {
            row.add(new KeyboardButton("5 nafar haydovchigacha = 160 000 сум"));
            row.add(new KeyboardButton("Cheklanmagan = 320 000 сум"));
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
//    public static SendMessage getСargocarMenu(Long chatId, String lang) {
//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        List<KeyboardRow> keyboard = new ArrayList<>();
//
//        KeyboardRow row = new KeyboardRow();
//        if (lang.equals("Русский")) {
//            row.add(new KeyboardButton("До 5-ти водителей = 280 000 сум"));
//            row.add(new KeyboardButton("Без ограничений = 560 000 сум"));
//        } else {
//            row.add(new KeyboardButton("5 nafar haydovchigacha = 280 000 сум"));
//            row.add(new KeyboardButton("Cheklanmagan = 560 000 сум"));
//        }
//
//        keyboard.add(row);
//        keyboardMarkup.setKeyboard(keyboard);
//
//        String text = lang.equals("Русский") ?
//                "Выберите вариант страхования:" :
//                "Sug‘urta variantini tanlang:";
//        SendMessage message = new SendMessage(chatId.toString(), text);
//        message.setReplyMarkup(keyboardMarkup);
//        return message;
//    }
//    public static SendMessage getInsuranceTypeMenu(Long chatId, String lang) {
//        return getMenu(chatId, lang); // просто вызывает твой getMenu
//    }


    // Меню для юридического лица  для Грузового
    public static SendMessage getLegalMenu(Long chatId, String lang) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        if (lang.equals("Русский")) {
            row.add(new KeyboardButton("До 5-ти водителей (280 000 сум)"));
            row.add(new KeyboardButton("Без ограничений = 560 000 сум"));
        } else {
            row.add(new KeyboardButton("5 ta haydovchigacha (280 000 сум)"));
            row.add(new KeyboardButton("Cheklanmagan = 560 000 сум"));
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

    public static SendMessage getConfirmMenu(Long chatId, String lang) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        if (lang.equals("Русский")) {
            row.add(new KeyboardButton("Подтвердить"));
        } else {
            row.add(new KeyboardButton("Tasdiqlash"));
        }

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        String text = lang.equals("Русский") ?
                "✅ Документы получены. Для начала страхования нажмите «Подтвердить»." :
                "✅ Hujjatlar qabul qilindi. Sug‘urtani boshlash uchun «Tasdiqlash» tugmasini bosing.";
        SendMessage message = new SendMessage(chatId.toString(), text);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }

    public static SendMessage getDocsMenu(Long chatId, String lang) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        if (lang.equals("Русский")) {
            row.add(new KeyboardButton("Отменить"));
        } else {
            row.add(new KeyboardButton("Bekor qilish"));
        }

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);

        String text = lang.equals("Русский") ?
                "📎 Перешлите техпаспорт автомобиля и паспорт владельца." :
                "📎 Avtomobil texpasporti va egasining pasportini yuboring.";
        SendMessage message = new SendMessage(chatId.toString(), text);
        message.setReplyMarkup(keyboardMarkup);
        return message;
    }










}
