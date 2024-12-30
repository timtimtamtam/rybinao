import com.sun.source.doctree.StartElementTree;

import java.sql.*;

public class Logic {

   private static final String DB_URL = "jdbc:sqlite:C:\\Users\\z\\Desktop\\bugs.db";

   public static String escapeMarkdown(String text) {
        if (text.isEmpty()){
            return "";
        }
        return text.replaceAll("[_*\\[\\]()~`>#+\\-=|{}.!]", "\\\\$0");
    }

    // Создание таблицы
    /*
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
    }*/

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
                    System.out.println(generatedKeys.getInt(1));
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
    public static String listBugsByTiket(String issueKey) {
        StringBuilder result = new StringBuilder();
        String selectQuery = "SELECT * FROM bugs3 WHERE issueKey = ?;";
        try (Connection con = DriverManager.getConnection(DB_URL);
            PreparedStatement st = con.prepareStatement(selectQuery)){
            st.setString(1, issueKey);
            try (ResultSet rs = st.executeQuery()) {
                result.append("```");
                result.append(String.format("%-4s | %-60s | %-10s | %-10s%n", "ID", "Описание", "Статус", "IssueKey"));
                result.append("\n");
                while (rs.next()) {
                   int bugId = rs.getInt("id");
                    String exem = Logic.listExamplByBugId(bugId);
                    result.append(String.format("%-4d | %-60s | %-10s | %-10s%n",
                            rs.getInt("id"),
                            rs.getString("description"),
                            rs.getString("status"),
                            rs.getString("issueKey")));
                    result.append("\nПримеры:\n").append(exem)
                            .append("\n");
                }
                result.append("```");

            }
        } catch (SQLException e ) {
            e.printStackTrace();
        }
        return result.toString().isEmpty()? "Список багов по задаче: "+ issueKey + " - пуст" : result.toString();
    }
    // Получение списка багов
    public static String listBugs() {
        StringBuilder result = new StringBuilder();
        String selectQuery = "SELECT * FROM bugs3";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {
            result.append("```\n");
            result.append(String.format("%-4s | %-60s | %-10s | %-10s%n", "ID", "Описание", "Статус", "IssueKey"));
            result.append("\n");

            while (rs.next()) {
                result.append(String.format("%-4d | %-60s | %-10s | %-10s%n",
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getString("issueKey") != null ? rs.getString("issueKey") : "null"));
            }
            result.append("```\n");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString().isEmpty() ? "Список багов пуст." : result.toString();
    }

    public static String listExamplByBugId (int bugId){
        StringBuilder result = new StringBuilder();
        String selectQuery = "SELECT * FROM examples WHERE bugId = ?;";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pst = conn.prepareStatement(selectQuery)){
            pst.setInt(1, bugId);
            try (ResultSet rs = pst.executeQuery()){
                while (rs.next()){
                    result.append(rs.getString("examples"))
                            .append("\n");
                }
            }
        }
        catch (SQLException e ) {
            e.printStackTrace();
        }
        return result.toString().isEmpty() ? "Список примеров пуст" : result.toString();
    }

    // Добавление примеров для бага
    public static void addExamp(int bugId, String exemp) {
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
    public static void updateBugStatus(int bugId, String newStatus) {
        String updateQuery = "UPDATE bugs3 SET status = ? WHERE id = ?;";
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

    public static void setUserRole(String userId, String role) {
        String insertQuery = "INSERT INTO users (userId, role) VALUES (?, ?);";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)){
            pstmt.setString(1, userId);
            pstmt.setString(2, role);
            pstmt.executeUpdate();
        } catch (SQLException e ){ e.printStackTrace();}
    }

    public static String getUserRole(String chatId) {
        StringBuilder result = new StringBuilder();
        String selectQuery = "SELECT role FROM users WHERE userId = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
            pstmt.setString(1, chatId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                result.append(rs.getString("role"));
            } else return "";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
