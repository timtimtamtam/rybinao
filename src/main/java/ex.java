import java.sql.*;

public class ex {
    private static final String EXDB_URL = "jdbc:sqlite:C:\\Users\\z\\Desktop\\bugs.db";

    public static void main(String[] args) {
        String insertBug = "INSERT INTO examples (bugId, examples) VALUES (?, ?)";
        String selectQuery = "SELECT * FROM examples";

        try (Connection conn = DriverManager.getConnection(EXDB_URL);
             PreparedStatement insertStmt = conn.prepareStatement(insertBug);
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {

            // Включаем поддержку внешних ключей
            Statement stmt = conn.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON;");

            // Вставляем данные
            insertStmt.setInt(1, 1);  // bugId = 1
            insertStmt.setString(2, "asdasdasdasdasdasdasdasd");  // Пример ошибки
            insertStmt.executeUpdate();

            // Чтение данных
            StringBuilder result = new StringBuilder();
            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("bugId"))
                        .append(", Description: ").append(rs.getString("examples"))
                        .append("\n");
            }

            System.out.println(result);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
