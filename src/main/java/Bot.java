import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {
    private final Map<String, String> userStates = new HashMap<>(); // Хранение состояния пользователя
    private final Map<String, Integer> task = new HashMap<>();
    @Override
    public String getBotUsername() {
        return "PongGameBot";
    }

    @Override
    public String getBotToken() {
        return "7698895747:AAEdQwZBJAujNXaSXkzuodK7Kq2ftyVPqxQ";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String userMessage = update.getMessage().getText();

            if (userMessage.equals("/start")) {
                sendInlineKeyboard(chatId);
            } else if ("waiting_for_bug_description".equals(userStates.get(chatId))) {
                int idBug = Logic.createBug(userMessage); // Создаем баг
                task.put(chatId, idBug);
                userStates.put(chatId, "w_i");
                sendMessage(chatId, "Введите issueKey задачи по которой создается баг");
            }else if ("w_i".equals(userStates.get(chatId))){
                int taskId = task.get(chatId);
                Logic.updateIssuKey(taskId, userMessage);
                sendMessage(chatId, "Баг заведен");
                userStates.remove(chatId);
                task.remove(chatId);
                sendInlineKeyboard(chatId);
            }else if ("waiting_for_new_status".equals(userStates.get(chatId))){
                if(userMessage.split(" ").length > 1){
                    String[] parts = userMessage.split(" ",2);
                    int bugId = Integer.parseInt(parts[0]);
                    String newStatus = parts[1];
                    String textStatus = newStatus.equals("1")?"Открыт":"Закрыт";
                    Logic.updateBug(bugId, textStatus);
                    sendMessage(chatId, "Стаус бага обновлен");
                    userStates.remove(chatId);
                    sendInlineKeyboard(chatId);
                }else {
                    sendMessage(chatId, "Некорректный ввод. Введите сначала Id бага, затем новый" +
                            " статус.\n 1 = открыт, 2 = решен.");
                    userStates.remove(chatId);
                    sendInlineKeyboard(chatId);
                }
            } else {
                sendMessage(chatId, "Используйте кнопки ниже для взаимодействия.");
                sendInlineKeyboard(chatId);
            }
        } else if (update.hasCallbackQuery()) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String callbackData = update.getCallbackQuery().getData();

            handleCallback(chatId, callbackData);
        }
    }

    // Метод для отправки сообщения с Inline-кнопками
    private void sendInlineKeyboard(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите действие:");

        // Создаем Inline-кнопки
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        // Первая строка кнопок
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Создать баг");
        button.setCallbackData("create_bug");
        row1.add(button);
        // Вторая строка кнопок
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Обновить баг");
        button2.setCallbackData("update_bug");
        row2.add(button2);
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Вывести список багов");
        button3.setCallbackData("list_bugs");
        row3.add(button3);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        inlineKeyboard.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboard);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    // Метод для обработки нажатий на Inline-кнопки
    private void handleCallback(String chatId, String callbackData) {
        switch (callbackData) {
            case "create_bug":
                sendMessage(chatId, "Введите описание нового бага.");
                userStates.put(chatId, "waiting_for_bug_description"); // Устанавливаем состояние
                break;
            case "list_bugs":
                sendMessage(chatId, Logic.listBugs());
                sendInlineKeyboard(chatId);
                break;
            case "update_bug":
                sendMessage(chatId, "Введите [ID бага] " +
                        "\n[новый статус] 1 = открыт, 2 = решен");
                userStates.put(chatId, "waiting_for_new_status");
                break;
            default:
                sendMessage(chatId, "Неизвестное действие.");
                break;
        }
    }

    // Универсальный метод для отправки текстовых сообщений
    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
            System.out.println("Бот успешно запущен!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
