import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRole {
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
