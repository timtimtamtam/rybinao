import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/*
"CREATE TABLE IF NOT EXISTS bugs2 (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "bugId INTEGER NOT NULL," +
                    "issueKey TEXT," +
                    "examples TEXT," +
                    "FOREIGN KEY (bugId) REFERENCES bugs(id)" +
                    ")";
*/
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
