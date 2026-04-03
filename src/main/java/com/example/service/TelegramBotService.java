package com.example.service;

import com.example.config.BotConfig;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import com.example.menu.InsuranceMenu;

import com.example.menu.StateManager;

import java.util.List;
import java.util.Arrays;
import java.util.Set;

import com.example.menu.*;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//import static com.example.menu.StateManager.fileTypes;

//import static com.example.menu.StateManager.fileTypes;

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

        Long chatId = null;

        if (update.hasMessage()) {
            System.out.println("Пришло текстовое сообщение: " + update.getMessage().getText());
        }
        if (update.hasCallbackQuery()) {
            System.out.println("Пришёл callback: " + update.getCallbackQuery().getData());
        }
        System.out.println("Текущее состояние: " + StateManager.getState(chatId));

        if (update.hasMessage() && update.getMessage().getNewChatMembers() != null && !update.getMessage().getNewChatMembers().isEmpty()) {
            chatId = update.getMessage().getChatId();
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
            chatId = update.getMessage().getChatId();
            String fileId = update.getMessage().getDocument().getFileId();
            StateManager.saveFile(chatId, fileId, "DOCUMENT");

            if (StateManager.getState(chatId).equals("WAIT_FILE")) {
                StateManager.setState(chatId, "PRINT_COPIES_CONFIRM");
                executeMessage(PrintMenu.confirmStep(chatId, StateManager.getLang(chatId)));
            } else if (StateManager.getState(chatId).equals("INSURANCE_DOCS")) {
                StateManager.appendDoc(chatId, "Документы получены");
                StateManager.setState(chatId, "INSURANCE_CONFIRM");
                executeMessage(InsuranceMenu.getConfirmMenu(chatId, StateManager.getLang(chatId)));


            } else {
                String lang = StateManager.getLang(chatId);
                if ("O‘zbekcha".equals(lang)) {
                    sendMessage(chatId, "📄 Hujjat qabul qilindi.");
                } else {
                    sendMessage(chatId, "📄 Файл получен.");
                }
            }
            return;
        }


        if (update.getMessage().hasPhoto()) {
            chatId = update.getMessage().getChatId();
            List<PhotoSize> photos = update.getMessage().getPhoto();
            if (!photos.isEmpty()) {
                String fileId = photos.get(photos.size() - 1).getFileId(); // берём самый большой размер
                StateManager.saveFile(chatId, fileId, "PHOTO");
            }
            if (StateManager.getState(chatId).equals("WAIT_FILE")) {
                StateManager.setState(chatId, "PRINT_COPIES_CONFIRM");
                executeMessage(PrintMenu.confirmStep(chatId, StateManager.getLang(chatId)));
            } else if (StateManager.getState(chatId).equals("INSURANCE_DOCS")) {
                StateManager.appendDoc(chatId, "Документы получены");
                StateManager.setState(chatId, "INSURANCE_CONFIRM");
                executeMessage(InsuranceMenu.getConfirmMenu(chatId, StateManager.getLang(chatId)));

            } else {
                String lang = StateManager.getLang(chatId);
                if ("O‘zbekcha".equals(lang)) {
                    sendMessage(chatId, "🖼️ Foto/screen qabul qilindi.");
                } else {
                    sendMessage(chatId, "🖼️ Фото/скриншоты получены.");
                }
            }
            return;

        }


        if (!update.getMessage().hasText()) return;

        chatId = update.getMessage().getChatId();
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
                executeMessage(PrintMenu.getFirstStep(chatId, StateManager.getLang(chatId)));
                break;

            case "Цветная печать":
            case "Черно-белая печать":
            case "Rangli chop etish":
            case "Qora-oq chop etish":
                StateManager.appendDoc(chatId, "Тип печати: " + text);
                StateManager.setState(chatId, "PRINT_FORMAT");
                executeMessage(PrintMenu.getFormatStep(chatId, StateManager.getLang(chatId)));
                break;

            case "В виде книжки":
            case "A4 ФОРМАТА":
            case "Kitob shaklida":
            case "A4 FORMATI":
                StateManager.appendDoc(chatId, "Формат: " + text);
                StateManager.setState(chatId, "PRINT_COPIES");
                executeMessage(PrintMenu.getCopiesStep(chatId, StateManager.getLang(chatId)));
                //sendMessage(chatId, "Введите количество копий ЦИФРОЙ:-1");
                break;



            case "Отменить":
            case "Bekor qilish":
                StateManager.resetDoc(chatId);
                StateManager.setState(chatId, "MAIN_MENU");
                executeMessage(MainMenu.getMenu(chatId, StateManager.getLang(chatId)));
                break;

            case "Страхование автомобиля":
            case "Avto sug'urta":
                StateManager.setState(chatId, "INSURANCE_TYPE");
                executeMessage(InsuranceMenu.getInsuranceTypeMenu(chatId, StateManager.getLang(chatId)));
                break;


            case "Легковое Авто":
            case "Engil Avto":
                StateManager.appendDoc(chatId, "Тип страховки: Легковое Авто");
                StateManager.setState(chatId, "INSURANCE_DRIVER_LIMIT");
                executeMessage(InsuranceMenu.getPhysicalMenu(chatId, StateManager.getLang(chatId)));
                break;

            case "Грузовое Авто":
            case "Yuk Avto":
                StateManager.appendDoc(chatId, "Тип страховки: Грузовое Авто");
                StateManager.setState(chatId, "INSURANCE_DRIVER_LIMIT");
                executeMessage(InsuranceMenu.getLegalMenu(chatId, StateManager.getLang(chatId)));
                break;

            case "До 5-ти водителей = 160 000 сум":
            case "5 nafar haydovchigacha = 160 000 сум":
                StateManager.appendDoc(chatId, "Вариант: До 5 человек");
                StateManager.setState(chatId, "INSURANCE_DOCS");
                executeMessage(InsuranceMenu.getDocsMenu(chatId, StateManager.getLang(chatId)));
                break;

            case "Без ограничений = 320 000 сум":
            case "Cheklanmagan = 320 000 сум":
                StateManager.appendDoc(chatId, "Вариант: Без ограничений");
                StateManager.setState(chatId, "INSURANCE_DOCS");
                executeMessage(InsuranceMenu.getDocsMenu(chatId, StateManager.getLang(chatId)));
                break;

            //для ГРУЗОВЫХ

            case "До 5-ти водителей (280 000 сум)":
            case "5 ta haydovchigacha (280 000 сум)":
                StateManager.appendDoc(chatId, "Вариант: До 5 человек");
                StateManager.setState(chatId, "INSURANCE_DOCS");
                executeMessage(InsuranceMenu.getDocsMenu(chatId, StateManager.getLang(chatId)));
                break;

            case "Без ограничений = 560 000 сум":
            case "Cheklanmagan = 560 000 сум":
                StateManager.appendDoc(chatId, "Вариант: Без ограничений");
                StateManager.setState(chatId, "INSURANCE_DOCS");
                executeMessage(InsuranceMenu.getDocsMenu(chatId, StateManager.getLang(chatId)));
                break;

            case "Отправить файл":
            case "Fayl yuborish":
                StateManager.setState(chatId, "WAIT_FILE");
                String langFile = StateManager.getLang(chatId);
                if ("O‘zbekcha".equals(langFile)) {
                    sendMessage(chatId, "📎 Iltimos, faylni yuboring (hujjat yoki foto).");
                } else {
                    sendMessage(chatId, "📎 Пожалуйста, отправьте файл (документ или фото).");
                }
                break;

            case "Подтвердить":
            case "Tasdiqlash":
                if (state.equals("INSURANCE_CONFIRM")) {
                    StateManager.setState(chatId, "INSURANCE_SUMMARY");
                    String langSum = StateManager.getLang(chatId);
                    if ("O‘zbekcha".equals(langSum)) {
                        sendMessage(chatId, "✅ Ariza qabul qilindi. Ma’lumotlar administratorga yuborildi.");
                    } else {
                        sendMessage(chatId, "✅ Заявка принята. Данные отправлены администратору.");
                    }
                    sendToAdmin(chatId, username, firstName, lastName);
                    StateManager.setState(chatId, "MAIN");
                    executeMessage(MainMenu.getMenu(chatId, StateManager.getLang(chatId)));

                } else if (state.equals("PRINT_CONFIRM")) {
                    StateManager.setState(chatId, "PRINT_NEXT");
                    executeMessage(PrintMenu.getSecondStep(chatId, StateManager.getLang(chatId)));

                } else if (state.equals("PRINT_CONFIRM2")) {
                    StateManager.setState(chatId, "PRINT_COPIES");
                    executeMessage(PrintMenu.getCopiesStep(chatId, StateManager.getLang(chatId)));

                } else if (state.equals("PRINT_COPIES_CONFIRM")) {
                    executeMessage(PrintMenu.getSummary(chatId, StateManager.getLang(chatId)));
                    sendToAdmin(chatId, username, firstName, lastName);

                } else if (state.equals("FILE_CONFIRM")) {
                    System.out.println("=== FILE_CONFIRM блок активирован ===");
                    String currentLang = StateManager.getLang(chatId);

                    if ("O‘zbekcha".equals(currentLang)) {
                        sendMessage(chatId, "✅ Matn tasdiqlandi. Administratorga yuborildi.");
                    } else {
                        sendMessage(chatId, "✅ Текст подтверждён. Отправлено администратору.");
                    }

                    sendToAdmin(chatId, username, firstName, lastName);

                    StateManager.resetDoc(chatId);
                    StateManager.resetTexts(chatId);

                    StateManager.setState(chatId, "MAIN");
                    executeMessage(MainMenu.getMenu(chatId, currentLang));
                }
                break;


//

            default:
                if (state.equals("PRINT_COPIES")) {
                    String langCopies = StateManager.getLang(chatId);
                    if ("O‘zbekcha".equals(langCopies)) {
                        StateManager.appendDoc(chatId, "Nusxalar soni: " + text);
                        StateManager.setState(chatId, "WAIT_FILE");
                        sendMessage(chatId, "📎 Chop etish uchun faylni yuboring");
                    } else {
                        StateManager.appendDoc(chatId, "Количество копий: " + text);
                        StateManager.setState(chatId, "WAIT_FILE");
                        sendMessage(chatId, "📎 Вложите файл для печати");
                    }

                } else if (state.equals("WAIT_FILE")) {
                    StateManager.saveText(chatId, text);

                    System.out.println("Меняю состояние на FILE_CONFIRM");
                    StateManager.setState(chatId, "FILE_CONFIRM");

                    String currentLang = StateManager.getLang(chatId);

                    sendMessage(chatId, currentLang.equals("O‘zbekcha")
                            ? "✏️ Matn qabul qilindi. Tasdiqlaysizmi?"
                            : "✏️ Текст получен. Подтвердите?");


                    executeMessage(com.example.menu.PrintMenu.confirmStep(chatId, currentLang));


                } else if (state.equals("FILE_CONFIRM")) {
                    System.out.println("=== FILE_CONFIRM блок активирован ===");
                    System.out.println("Получено сообщение: '" + text + "'");
                    System.out.println("Тексты в StateManager: " + StateManager.getTexts(chatId));
                    System.out.println("Документ в StateManager: " + StateManager.getDoc(chatId));


                    if (text.trim().equalsIgnoreCase("Подтвердить") || text.trim().equalsIgnoreCase("Tasdiqlash")) {
                        System.out.println("Условие подтверждения выполнено");

                        String currentLang = StateManager.getLang(chatId);

                        if ("O‘zbekcha".equals(currentLang)) {
                            sendMessage(chatId, "✅ Matn tasdiqlandi. Administratorga yuborildi.");
                        } else {
                            sendMessage(chatId, "✅ Текст подтверждён. Отправлено администратору.");
                        }

                        // Отправляем техт админу
                        sendToAdmin(chatId, username, firstName, lastName);

                        // Сбрасываем данные заказа(после отправки)
                        StateManager.resetDoc(chatId);
                        StateManager.resetTexts(chatId);

                        // Возврат в главное меню
                        StateManager.setState(chatId, "MAIN");
                        executeMessage(MainMenu.getMenu(chatId, currentLang));

                    } else if (text.trim().equalsIgnoreCase("Отменить") || text.trim().equalsIgnoreCase("Bekor qilish")) {
                        System.out.println("Условие отмены выполнено");

                        StateManager.resetDoc(chatId);
                        StateManager.resetTexts(chatId);
                        StateManager.setState(chatId, "MAIN");

                        String currentLang = StateManager.getLang(chatId);

                        if ("O‘zbekcha".equals(currentLang)) {
                            sendMessage(chatId, "❌ Bekor qilindi. Asosiy menyuga qaytdingiz.");
                        } else {
                            sendMessage(chatId, "❌ Отменено. Вы вернулись в главное меню.");
                        }

                        executeMessage(MainMenu.getMenu(chatId, currentLang));
                    }
                } else if (state.equals("INSURANCE_DOCS")) {
                    if (update.getMessage().hasDocument()) {
                        String fileId = update.getMessage().getDocument().getFileId();
                        StateManager.saveFile(chatId, fileId, "DOCUMENT");
                    } else if (update.getMessage().hasPhoto()) {
                        List<PhotoSize> photos = update.getMessage().getPhoto();
                        if (!photos.isEmpty()) {
                            String fileId = photos.get(photos.size() - 1).getFileId();
                            StateManager.saveFile(chatId, fileId, "PHOTO");
                        }
                    } else {
                        executeMessage(InsuranceMenu.getDocsMenu(chatId, StateManager.getLang(chatId)));
                        return;
                    }

                    StateManager.appendDoc(chatId, "Документы получены");
                    StateManager.setState(chatId, "INSURANCE_CONFIRM");
                    executeMessage(InsuranceMenu.getConfirmMenu(chatId, StateManager.getLang(chatId)));

                } else if (state.equals("INSURANCE_CONFIRM")) {
                    StateManager.appendDoc(chatId, "Выбран пакет: " + text);
                    StateManager.setState(chatId, "INSURANCE_SUMMARY");
                    String langSum = StateManager.getLang(chatId);
                    if ("O‘zbekcha".equals(langSum)) {
                        sendMessage(chatId, "✅ Ariza qabul qilindi. Ma’lumotlar administratorga yuborildi.");
                    } else {
                        sendMessage(chatId, "✅ Заявка принята. Данные отправлены администратору.");
                    }
                    sendToAdmin(chatId, username, firstName, lastName);

                } else {
                    String langDef = StateManager.getLang(chatId);
                    if ("O‘zbekcha".equals(langDef)) {
                        sendMessage(chatId, "Iltimos, menyudan tanlang.");
                    } else {
                        sendMessage(chatId, "Пожалуйста, выберите пункт меню.");
                    }
                }
        }
    }


    private void sendToAdmin(Long chatId, String username, String firstName, String lastName) {
        System.out.println("sendToAdmin вызван");
        Long adminId = config.getAdminId();
        String userDoc = StateManager.getDoc(chatId);

        // Заголовок заказа
        StringBuilder sb = new StringBuilder();
        sb.append("📩 Новый заказ!\n\n");
        sb.append("👤 Пользователь:\n");
        sb.append("• Chat ID: ").append(chatId).append("\n");
        sb.append("• Username: @").append(username != null ? username : "нет").append("\n");
        sb.append("• Имя: ").append(firstName != null ? firstName : "нет").append("\n");
        sb.append("• Фамилия: ").append(lastName != null ? lastName : "нет").append("\n\n");
        sb.append("🧾 Параметры заказа:\n");
        sb.append(userDoc);

        // Отправляем админу текст с параметрами
        executeMessage(new SendMessage(adminId.toString(), sb.toString()));

        // Пересылаем документы и фото
        Set<String> files = StateManager.getFiles(chatId);
        for (String fId : files) {
            String type = StateManager.getFileType(chatId, fId);
            if ("DOCUMENT".equals(type)) {
                SendDocument doc = new SendDocument();
                doc.setChatId(adminId.toString());
                doc.setDocument(new InputFile(fId));
                executeMessage(doc);
            } else if ("PHOTO".equals(type)) {
                SendPhoto photo = new SendPhoto();
                photo.setChatId(adminId.toString());
                photo.setPhoto(new InputFile(fId));
                executeMessage(photo);
            }
        }

        // Пересылаем текстовые сообщения отдельно

        List<String> texts = StateManager.getTexts(chatId);
        System.out.println("Тексты для отправки админу: " + texts);
        for (String txt : texts) {
            System.out.println("Отправляем текст админу: " + txt);
            SendMessage msg = new SendMessage(adminId.toString(), "📝 Текст:\n" + txt);
            executeMessage(msg);
        }
//        for (String txt : StateManager.getTexts(chatId)) {
//            System.out.println("Отправляем текст админу: " + txt);
//            SendMessage msg = new SendMessage(adminId.toString(), "📝 Текст:\n" + txt);
//            executeMessage(msg);
//        }
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


