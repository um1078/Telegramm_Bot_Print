package com.example.service;

import com.example.config.BotConfig;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import java.util.List;
import java.util.Arrays;

import com.example.menu.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TelegramBotService extends TelegramLongPollingBot {
    private final BotConfig config;

    public TelegramBotService(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().getNewChatMembers() != null && !update.getMessage().getNewChatMembers().isEmpty()) {
            Long chatId = update.getMessage().getChatId();
            SendPoll poll = new SendPoll();
            poll.setChatId(chatId.toString());
            poll.setQuestion("Добро пожаловать! Выберите язык интерфейса:");
            poll.setOptions(Arrays.asList("Русский", "O‘zbekcha", "English"));
            poll.setIsAnonymous(false);
            try {
                execute(poll);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }

        if (update.getMessage().hasDocument()) {
            Long chatId = update.getMessage().getChatId();
            String fileId = update.getMessage().getDocument().getFileId();

            StateManager.saveFile(chatId, fileId, "DOCUMENT");

            if (StateManager.getState(chatId).equals("WAIT_FILE")) {
                StateManager.setState(chatId, "PRINT_COPIES_CONFIRM");
                executeMessage(PrintMenu.confirmStep(chatId));
            } else {
                sendMessage(chatId, "Файл получен.");
            }
            return;
        }

        if (update.getMessage().hasPhoto()) {
            Long chatId = update.getMessage().getChatId();
            update.getMessage().getPhoto().forEach(photoSize -> {
                String fileId = photoSize.getFileId();
                StateManager.saveFile(chatId, fileId, "PHOTO");
            });
            if (StateManager.getState(chatId).equals("WAIT_FILE")) {
                StateManager.setState(chatId, "PRINT_COPIES_CONFIRM");
                executeMessage(PrintMenu.confirmStep(chatId));
            } else {
                sendMessage(chatId, "Фото/скриншоты получены.");
            }
            return;
        }

        if (!update.getMessage().hasText()) return;

        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();

        String text = update.getMessage().getText();
        String state = StateManager.getState(chatId);

        switch (text) {
            case "/start":
                StateManager.setState(chatId, "LANGUAGE");
                executeMessage(LanguageMenu.getMenu(chatId));
                break;

            case "Русский":
            case "O‘zbekcha":
                StateManager.setLang(chatId, text);
                StateManager.setState(chatId, "MAIN");
                executeMessage(MainMenu.getMenu(chatId, text));
                break;

            case "Начать":
            case "Boshlash":
            case "Старт":
                StateManager.resetDoc(chatId);
                StateManager.setState(chatId, "ACTION");
                String lang = StateManager.getLang(chatId);
                executeMessage(ActionMenu.getMenu(chatId, lang));
                break;

            case "Закончить":
            case "Tugatish":
                StateManager.setState(chatId, "LANGUAGE");
                executeMessage(LanguageMenu.getMenu(chatId));
                break;

            case "Распечатать документ":
            case "Hujjatni chop etish":
                StateManager.setState(chatId, "PRINT");
                executeMessage(PrintMenu.getFirstStep(chatId));
                break;

            case "Цветная печать":
            case "Черно-белая печать":
            case "Rangli chop etish":
            case "Qora-oq chop etish":
                StateManager.appendDoc(chatId, "Тип печати: " + text);
                StateManager.setState(chatId, "PRINT_CONFIRM");
                executeMessage(PrintMenu.confirmStep(chatId));
                break;

            case "В виде книжки":
            case "A4 ФОРМАТА":
            case "Kitob shaklida":
            case "A4 FORMATI":
                StateManager.appendDoc(chatId, "Формат: " + text);
                StateManager.setState(chatId, "PRINT_CONFIRM2");
                executeMessage(PrintMenu.confirmStep(chatId));
                break;

            case "Подтвердить":
            case "Tasdiqlash":
                if (state.equals("PRINT_CONFIRM")) {
                    StateManager.setState(chatId, "PRINT_NEXT");
                    executeMessage(PrintMenu.getSecondStep(chatId));
                } else if (state.equals("PRINT_CONFIRM2")) {
                    StateManager.setState(chatId, "PRINT_COPIES");
                    executeMessage(PrintMenu.getCopiesStep(chatId));
                } else if (state.equals("PRINT_COPIES_CONFIRM")) {
                    executeMessage(PrintMenu.getSummary(chatId));
                    sendToAdmin(chatId, username, firstName, lastName);
                }
                break;

            case "Отменить":
            case "Bekor qilish":
                StateManager.resetDoc(chatId);
                StateManager.setState(chatId, "MAIN_MENU");
                executeMessage(MainMenu.getMenu(chatId, StateManager.getLang(chatId)));
                break;

            default:
                if (state.equals("PRINT_COPIES")) {
                    StateManager.appendDoc(chatId, "Количество копий: " + text);
                    StateManager.setState(chatId, "WAIT_FILE");
                    sendMessage(chatId, "📎 Вложите файл для печати");
                } else {
                    sendMessage(chatId, "Пожалуйста, выберите пункт меню.");
                }
        }
    }

    private void sendToAdmin(Long chatId, String username, String firstName, String lastName) {
        Long adminId = config.getAdminId();
        String userDoc = StateManager.getDoc(chatId);

        StringBuilder sb = new StringBuilder();
        sb.append("📩 Новый заказ!\n\n");
        sb.append("👤 Пользователь:\n");
        sb.append("• Chat ID: ").append(chatId).append("\n");
        sb.append("• Username: @").append(username != null ? username : "нет").append("\n");
        sb.append("• Имя: ").append(firstName != null ? firstName : "нет").append("\n");
        sb.append("• Фамилия: ").append(lastName != null ? lastName : "нет").append("\n\n");
        sb.append("🧾 Параметры заказа:\n");
        sb.append(userDoc);

        executeMessage(new SendMessage(adminId.toString(), sb.toString()));

        List<String> files = StateManager.getFiles(chatId);
        String fileType = StateManager.getFileType(chatId);

        if (!files.isEmpty()) {
            for (String fId : files) {
                if ("DOCUMENT".equals(fileType)) {
                    SendDocument doc = new SendDocument();
                    doc.setChatId(adminId.toString());
                    doc.setDocument(new InputFile(fId));
                    executeMessage(doc);
                } else if ("PHOTO".equals(fileType)) {
                    SendPhoto photo = new SendPhoto();
                    photo.setChatId(adminId.toString());
                    photo.setPhoto(new InputFile(fId));
                    executeMessage(photo);
                }
            }
        }
    }

    private void executeMessage(SendPhoto photo) {
        try {
            execute(photo);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void executeMessage(SendDocument document) {
        try {
            execute(document);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId.toString(), text);
        executeMessage(message);
    }
}
