package com.example.menu;

import java.util.*;

public class StateManager {

    private static final Map<Long, String> userStates = new HashMap<>();
    private static final Map<Long, StringBuilder> userDocs = new HashMap<>();
    private static final Map<Long, String> userLang = new HashMap<>();

    // ------------------ TEXT ------------------
    private static final Map<Long, List<String>> textMessages = new HashMap<>();

    public static void saveText(Long chatId, String text) {
        // Убираем пробелы и приводим к нижнему регистру
        String normalized = text.trim().toLowerCase();

        // Список служебных команд, которые не нужно сохранять
        List<String> serviceWords = Arrays.asList(
                "подтвердить", "отменить", "tasdiqlash", "bekor qilish"
        );

        // Если текст не является служебным словом — сохраняем
        if (!serviceWords.contains(normalized)) {
            textMessages.computeIfAbsent(chatId, k -> new ArrayList<>()).add(text);
            System.out.println("Сохранён текст: " + text);
        } else {
            System.out.println("Игнорирован служебный текст: " + text);
        }
    }
    // Очистка файлов после отправки админу
    public static void resetFiles(Long chatId) {
        fileIds.put(chatId, new HashSet<>());     // очищаем список fileId
        fileTypes.put(chatId, new HashMap<>());   // очищаем типы файлов
    }


//
//    public static void saveText(Long chatId, String text) {
//        textMessages.computeIfAbsent(chatId, k -> new ArrayList<>()).add(text);
//    }

    public static List<String> getTexts(Long chatId) {
        return textMessages.getOrDefault(chatId, new ArrayList<>());
    }

    // Очистка текстов после отправки админу
    public static void resetTexts(Long chatId) {
        textMessages.put(chatId, new ArrayList<>());
    }

    // ------------------ FILE ------------------
    private static final Map<Long, Map<String, String>> fileTypes = new HashMap<>();
    private static final Map<Long, Set<String>> fileIds = new HashMap<>();

    public static void saveFile(Long chatId, String fileId, String type) {
        fileIds.computeIfAbsent(chatId, k -> new HashSet<>()).add(fileId);
        fileTypes.computeIfAbsent(chatId, k -> new HashMap<>()).put(fileId, type);
    }

    public static Set<String> getFiles(Long chatId) {
        return fileIds.getOrDefault(chatId, new HashSet<>());
    }

    public static String getFileType(Long chatId, String fileId) {
        Map<String, String> types = fileTypes.get(chatId);
        if (types == null) return null;
        return types.get(fileId);
    }

    // ------------------ STATE ------------------
    public static void setState(Long chatId, String state) {
        userStates.put(chatId, state);
        System.out.println("Состояние для " + chatId + " изменено на: " + state);
    }

    public static String getState(Long chatId) {
        return userStates.getOrDefault(chatId, "LANGUAGE");
    }

    // ------------------ LANGUAGE ------------------
    public static void setLang(Long chatId, String lang) {
        userLang.put(chatId, lang);
    }

    public static String getLang(Long chatId) {
        return userLang.getOrDefault(chatId, "Русский");
    }

    // ------------------ DOCUMENT (ORDER DATA) ------------------
    public static void appendDoc(Long chatId, String text) {
        userDocs.putIfAbsent(chatId, new StringBuilder());
        userDocs.get(chatId).append(text).append("\n");
    }

    public static String getDoc(Long chatId) {
        return userDocs.getOrDefault(chatId, new StringBuilder()).toString();
    }

    public static void resetDoc(Long chatId) {
        userDocs.put(chatId, new StringBuilder());
    }
}
