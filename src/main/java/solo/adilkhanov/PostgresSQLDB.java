package solo.adilkhanov;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresSQLDB {
    public static String URL = PropertiesReader.getProperties("postgresql.url");
    public static String USER = PropertiesReader.getProperties("postgresql.username");
    public static String PASSWORD = PropertiesReader.getProperties("postgresql.password");

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
