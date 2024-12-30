import java.util.HashMap;
import java.util.Map;


public class CallbackHandler {
    public static Map<String, String> userStates = new HashMap<>(); // Хранение состояния пользователя
    // Метод для обработки нажатий на Inline-кнопки
    public static void handleCallback(String chatId, String callbackData, Bot bot) {
        String userRole = Logic.getUserRole(chatId);
        switch (callbackData) {
            case "test":
                Logic.setUserRole(chatId, "test");
                bot.sendMessage(chatId, "Установлена роль: Тестировщик");
                MenuManager.sendInlineKeyboard(chatId, bot);
                break;
            case "dev":
                Logic.setUserRole(chatId, "dev");
                bot.sendMessage(chatId, "Установлена роль: Разработчик");
                MenuManager.sendDevMenu(chatId, bot);
                break;
            case "add_exampl_for_new_bug":
                bot.sendMessage(chatId,"Введите пример для бага");
                userStates.put(chatId, "exampl_waiting");
                break;
            case "send_base_menu":
                MenuManager.sendInlineKeyboard(chatId, bot);
                userStates.remove(chatId);
            case "create_bug":
                bot.sendMessage(chatId, "Введите описание нового бага.");
                userStates.put(chatId, "waiting_for_bug_description"); // Устанавливаем состояние
                break;
            case "list_bugs":
                bot.sendMessageMarkDown(chatId, Logic.listBugs());
                if (userRole.equals("test")) { MenuManager.sendInlineKeyboard(chatId, bot); }
                else { MenuManager.sendDevMenu(chatId, bot); }
                break;
            case "list_bugs_by_tiket":
                bot.sendMessage(chatId, "Введите issueKey тикета по которому требуется" +
                        " вывести список багов");
                userStates.put(chatId, "waiting_issueKey_for_listing");
                break;
            case "close_bug":
                bot.sendMessage(chatId, "Введите ID бага который переводится в статус \"Закрыт\"" +
                        "\nID бага можно узнать" +
                        " используя команды - \"Вывести список багов по тикету\" или \"Вывести список багов\"");
                userStates.put(chatId, "waiting_id_bug_for_new_status");
                break;
            default:
                bot.sendMessage(chatId, "Неизвестное действие.");
                break;
        }
    }
}
