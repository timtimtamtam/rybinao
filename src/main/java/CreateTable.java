import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    private static final String EXDB_URL = "jdbc:sqlite:C:\\Users\\z\\Desktop\\bugs.db";
    public static void main(String[] args) {
        String createTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId TEXT NOT NULL," +
                "role TEXT" +
                ");";
        try (Connection conn = DriverManager.getConnection(EXDB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.execute(createTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
/*
"CREATE TABLE IF NOT EXISTS bugs2 (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "bugId INTEGER NOT NULL," +
                    "issueKey TEXT," +
                    "examples TEXT," +
                    "FOREIGN KEY (bugId) REFERENCES bugs(id)" +
                    ")";

public class a {
    private static final String EXDB_URL = "jdbc:sqlite:C:\\Users\\z\\Desktop\\bugs.db";
    public static void main(String[] args) {
        String createTable = "CREATE TABLE IF NOT EXISTS bugs3 (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "description TEXT NOT NULL," +
                "status TEXT DEFAULT 'Открыт'," +
                "issueKey TEXT" +
                ");";
            try (Connection conn = DriverManager.getConnection(EXDB_URL);
                 Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
                stmt.execute(createTable);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
*/

/*    private static final String EXDB_URL = "jdbc:sqlite:C:\\Users\\z\\Desktop\\bugs.db";

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
    }*/