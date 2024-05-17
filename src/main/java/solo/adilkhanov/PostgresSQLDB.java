package solo.adilkhanov;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresSQLDB {
    public static String URL = "jdbc:postgresql://localhost:5432/postgres";
    public static String USER = "postgres";
    public static String PASSWORD = "Astana12";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
