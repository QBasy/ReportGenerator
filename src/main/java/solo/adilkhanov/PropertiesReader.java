package solo.adilkhanov;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
    private static final Properties properties = new Properties();;
    private static final FileInputStream fileInputStream;

    static {
        try {
            fileInputStream = new FileInputStream("./src/main/etc/generate.properties");
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getProperties(String key) {
        return properties.getProperty(key);
    }
}
