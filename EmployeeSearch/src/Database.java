import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Database {

    private Connection conn;

    public boolean connect(String propsFile) {
        try {
            FileReader reader = new FileReader(propsFile);
            Properties p = new Properties();
            p.load(reader);

            String url = p.getProperty("db.url");
            String user = p.getProperty("db.user");
            String password = p.getProperty("db.password");
            String driver = p.getProperty("db.driver");

            Class.forName(driver);

            conn = DriverManager.getConnection(url, user, password);
            return true;

        } catch (Exception e) {
            System.out.println("Database connection failed:");
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() {
        return conn;
    }
}
