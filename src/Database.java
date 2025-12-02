import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Database {

    private Connection conn;
    private Exception lastException;

    public boolean connect(String propsFile) {
        try {
            Properties p = new Properties();

            // Try the exact path first, then the EmployeeSearch subfolder, then classpath
            File f = new File(propsFile);
            if (f.exists()) {
                try (FileReader reader = new FileReader(f)) {
                    p.load(reader);
                }
            } else {
                File f2 = new File("EmployeeSearch/" + propsFile);
                if (f2.exists()) {
                    try (FileReader reader = new FileReader(f2)) {
                        p.load(reader);
                    }
                } else {
                    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(propsFile);
                    if (in == null) {
                        throw new FileNotFoundException("Properties file not found: " + propsFile);
                    }
                    try (InputStream bin = in) {
                        p.load(bin);
                    }
                }
            }

            String url = p.getProperty("db.url");
            String user = p.getProperty("db.user");
            String password = p.getProperty("db.password");
            String driver = p.getProperty("db.driver");

            Class.forName(driver);

            conn = DriverManager.getConnection(url, user, password);
            return true;

        } catch (Exception e) {
            lastException = e;
            System.out.println("Database connection failed:");
            e.printStackTrace();
            return false;
        }
    }

    public Connection getConnection() {
        return conn;
    }

    /**
     * Return the last exception (if any) that occurred during connect().
     */
    public String getLastError() {
        if (lastException == null) return null;
        String msg = lastException.getMessage();
        return (msg == null || msg.isEmpty()) ? lastException.toString() : msg;
    }
}
