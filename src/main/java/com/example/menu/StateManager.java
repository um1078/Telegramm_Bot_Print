package com.example.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateManager {

    private static final Map<Long, String> userStates = new HashMap<>();
    private static final Map<Long, StringBuilder> userDocs = new HashMap<>();
    private static final Map<Long, String> userLang = new HashMap<>();
    private static final Map<Long, String> fileMap = new HashMap<>();
    private static Map<Long, List<String>> fileIds = new HashMap<>();
    private static Map<Long, String> fileTypes = new HashMap<>();

    // ------------------ STATE ------------------
    public static void setState(Long chatId, String state) {
        userStates.put(chatId, state);
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

    // ------------------ FILE ------------------
    public static void saveFile(Long chatId, String fileId, String type) {
        fileIds.computeIfAbsent(chatId, k -> new ArrayList<>()).add(fileId);
        fileTypes.put(chatId, type);
    }

    public static List<String> getFiles(Long chatId) {
        return fileIds.getOrDefault(chatId, new ArrayList<>());
    }

    public static String getFileType(Long chatId) {
        return fileTypes.get(chatId);
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
