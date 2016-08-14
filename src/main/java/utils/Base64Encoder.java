package utils;

import java.io.IOException;
import java.util.Base64;

public final class Base64Encoder {

    public static final String ENCODING = "UTF-8";
    
    public byte[] base64Decode(String property) {
        return Base64.getDecoder().decode(property);
    }

    public String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes).trim();
    }
}
