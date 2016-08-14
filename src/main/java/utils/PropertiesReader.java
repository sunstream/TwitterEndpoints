package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import static org.junit.Assert.fail;

public enum PropertiesReader {
    PROPERTIES;

    private static final String CONFIG_FILE_NAME = "config.properties";
    private final Properties prop = new Properties();
    private final EncryptedDataAccessor encryptedDataAccessor = new EncryptedDataAccessor();
    PropertiesReader(){
        InputStream inputStream = PropertiesReader.class
                .getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            fail("Failed to read configuration file "+CONFIG_FILE_NAME);
        }
    }

    public static String getPlainProperty(String key){
        String result = PROPERTIES.prop.getProperty(key);
        if (result == null){
            fail("Property by key '"+key+"' was not found.");
        }
        return result;
    }

    public static String getEncodedProperty(String key){
        String encodedValue = getPlainProperty(key);
        return Objects.isNull(encodedValue)
                ? encodedValue
                : PROPERTIES.encryptedDataAccessor.decrypt(encodedValue);
    }
}
