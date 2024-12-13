import java.sql.*;

public class Logic {

    private static final String DB_URL = "jdbc:sqlite:C:\\Users\\z\\Desktop\\bugs.db";

    // Создание таблицы
    public static void initialize() {
        String createTable = "CREATE TABLE IF NOT EXISTS bugs2 (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "description TEXT NOT NULL," +
                "examples TEXT," +
                "status TEXT DEFAULT 'Открыт'" +
                ")";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Создание бага
    public static int createBug(String description) {
        String insertBug = "INSERT INTO bugs3 (description) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertBug, Statement.RETURN_GENERATED_KEYS)) {

            // Установка параметра
            pstmt.setString(1, description);

            // Выполнение запроса
            pstmt.executeUpdate();

            // Получение сгенерированного ключа
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Возвращаем ID созданного бага
                } else {
                    throw new SQLException("Создание бага не вернуло ID.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Возврат -1 в случае ошибки
        }
    }

    // Получение списка багов
    public static String listBugs() {
        StringBuilder result = new StringBuilder();
        String selectQuery = "SELECT * FROM bugs3";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {
            while (rs.next()) {
                result.append("ID: ").append(rs.getInt("id"))
                        .append(", Description: ").append(rs.getString("description"))
                        .append(", Status: ").append(rs.getString("status"))
                        .append(", IssueKey: ").append(rs.getString("issueKey"))
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString().isEmpty() ? "Список багов пуст." : result.toString();
    }

    // Добавление примеров в баг
    public static void updateExemp(int bugId, String exemp) {
        String insertExemp = "INSERT INTO examples (bugId, examples) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertExemp)) {
            pstmt.setInt(1, bugId);
            pstmt.setString(2, exemp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Обновление статуса бага
    public static void updateBug(int bugId, String newStatus) {
        String updateQuery = "UPDATE bugs3 SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, bugId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateIssuKey(int bugId, String issueKey) {
        String updateQuery = "UPDATE bugs3 SET issueKey = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, issueKey);
            pstmt.setInt(2, bugId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}