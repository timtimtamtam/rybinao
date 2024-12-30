import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

// Класс для создания меню
public class MenuManager {

    // Метод для отправки меню для работы с багом
    public static void sendBugMenu(String chatId, Bot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите действие:");
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setCallbackData("add_exampl_for_new_bug");
        button.setText("Добавить пример для бага");
        row1.add(button);

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setCallbackData("send_base_menu");
        button1.setText("Вывести основное меню");
        row1.add(button1);

        keyboard.add(row1);
        inlineKeyboard.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboard);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    // Метод для отправки меню для пользователя с ролью -"Разработчик"
    public static void sendDevMenu (String chatId, Bot bot){

        SendMessage message = new SendMessage();
        message.setText("Выберите действие: ");
        message.setChatId(chatId);

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Вывести список багов");
        button.setCallbackData("list_bugs");
        row.add(button);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Вывести список багов по тикету");
        button2.setCallbackData("list_bugs_by_tiket");
        row2.add(button2);

        keyboard.add(row);
        keyboard.add(row2);

        inlineKeyboard.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboard);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    // Метод для отправки  меню с кнопками смены статуса бага

    public static void sendBagStatusMenu(String chatId, Bot bot) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите новый статус бага");

        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Открыт");
    }
    // Метод для отправки сообщения с Inline-кнопками основного меню
    public static void sendInlineKeyboard(String chatId, Bot bot) {
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
        button2.setText("Закрыть баг");
        button2.setCallbackData("close_bug");
        row2.add(button2);
        // Третья строка кнопок
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Вывести список багов");
        button3.setCallbackData("list_bugs");
        row3.add(button3);

        List<InlineKeyboardButton> row4 = new ArrayList<>();
        InlineKeyboardButton button4 = new InlineKeyboardButton();
        button4.setText("Вывести список багов по тикету");
        button4.setCallbackData("list_bugs_by_tiket");
        row4.add(button4);

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);

        inlineKeyboard.setKeyboard(keyboard);
        message.setReplyMarkup(inlineKeyboard);

        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
