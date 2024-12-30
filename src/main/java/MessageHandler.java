import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler {
    public static void sendMenuRole(String chatId,Bot bot){
        //Отправили месседж
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Выберите роль:");
        //Создали кнопки
        InlineKeyboardMarkup ik = new InlineKeyboardMarkup();
        //Создали клаву
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        //Строка с кнопками
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();

        button.setText("Разраб");
        button.setCallbackData("dev");
        row1.add(button);

        InlineKeyboardButton button2 = new InlineKeyboardButton();

        button2.setText("Тестер");
        button2.setCallbackData("test");
        row1.add(button2);
        keyboard.add(row1);
        ik.setKeyboard(keyboard);
        message.setReplyMarkup(ik);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
