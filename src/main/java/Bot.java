import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {
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
            String userRole = Logic.getUserRole(chatId);

            if (userMessage.equals("/start")) {
                if (userRole.equals("")) {
                    MessageHandler.sendMenuRole(chatId, this);
                    return;
                } else if(userRole.equals("test")){
                    MenuManager.sendInlineKeyboard(chatId, this);
                } else if(userRole.equals("dev")){
                    MenuManager.sendDevMenu(chatId, this);
                }
            } else if ("waiting_issueKey_for_listing".equals(CallbackHandler.userStates.get(chatId))) {
                String[] um = userMessage.split(" ");
                if (um.length < 1) {
                    sendMessage(chatId, "Ошибка ввода");
                    return;
                }

                String issueId = um[0];
                if (!issueId.isEmpty()) {
                    String issueKey = "BPM-" + issueId;
                    sendMessageMarkDown(chatId, (Logic.listBugsByTiket(issueKey)));
                    CallbackHandler.userStates.remove(chatId);
                    if (userRole.equals("test")) { MenuManager.sendInlineKeyboard(chatId, this); }
                    else {MenuManager.sendDevMenu(chatId, this);}
                }
            } else if ("waiting_for_bug_description".equals(CallbackHandler.userStates.get(chatId))) {
                task.remove(chatId);
                int idBug = Logic.createBug(userMessage); // Создаем баг
                task.put(chatId, idBug);
                CallbackHandler.userStates.put(chatId, "w_i");
                sendMessage(chatId, "Введите issueKey задачи по которой создается баг");
            } else if ("w_i".equals(CallbackHandler.userStates.get(chatId))) {
                int taskId = task.get(chatId);
                Logic.updateIssuKey(taskId, userMessage);
                sendMessage(chatId, "Баг заведен");
                CallbackHandler.userStates.remove(chatId);
                MenuManager.sendBugMenu(chatId, this);
            }else if ("exampl_waiting".equals(CallbackHandler.userStates.get(chatId))){
                int taskId = task.get(chatId);
                Logic.addExamp(taskId, userMessage);
                task.remove(chatId);
                CallbackHandler.userStates.remove(chatId);
                MenuManager.sendInlineKeyboard(chatId, this);
            }else if ("waiting_id_bug_for_new_status".equals(CallbackHandler.userStates.get(chatId))){
                int lengthUserMessage = userMessage.split(" ").length;
                if(lengthUserMessage == 1){
                    String[] parts = userMessage.split(" ",1);
                    int bugId = Integer.parseInt(parts[0]);
                    Logic.updateBugStatus(bugId, "Закрыт");
                    CallbackHandler.userStates.remove(chatId);
                    sendMessage(chatId, "Баг с id - " + bugId + " успешно переведен в статус \"Закрыт\"");
                    MenuManager.sendInlineKeyboard(chatId, this);
                }else {
                    sendMessage(chatId, "Некорректный ввод.");
                    CallbackHandler.userStates.remove(chatId);
                    MenuManager.sendInlineKeyboard(chatId,this);
                }
            } else {
                sendMessage(chatId, "Используйте кнопки ниже для взаимодействия.");
                MenuManager.sendInlineKeyboard(chatId, this);
            }
        } else if (update.hasCallbackQuery()) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String callbackData = update.getCallbackQuery().getData();
            CallbackHandler.handleCallback(chatId, callbackData, this);
        }
    }
    public static void main (String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
            System.out.println("Бот успешно запущен!");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    // Универсальный метод для отправки текстовых сообщений
    public void sendMessageMarkDown(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setParseMode("MARKDOWNV2");
        message.enableMarkdown(true);
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
