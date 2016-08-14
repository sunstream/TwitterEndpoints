package utils;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.fail;
import static utils.Base64Encoder.ENCODING;

final class EncryptedDataAccessor implements CommonErrorMessages {
    private static final char[] PASSWORD = "enfldsgbnlsngdlksdsgm".toCharArray();
    private static final byte[] SALT = {
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
            (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };
    private static final String CIPHER_ALGORITHM = "PBEWithMD5AndDES";

    private Cipher cipher;
    private SecretKey cipherKey;
    private final AlgorithmParameterSpec cipherParameterSpec;
    private final Base64Encoder coder = new Base64Encoder();

    public EncryptedDataAccessor(){
        SecretKeyFactory cipherKeyFactory;
        try {
            cipherKeyFactory = SecretKeyFactory.getInstance(CIPHER_ALGORITHM);
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipherKey = cipherKeyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException e) {
            fail(byException(e) + "Failed to setup PBE Cipher.");
        }
        cipherParameterSpec = new PBEParameterSpec(SALT, 20);
    }

    public String encrypt(String property) {
        initCipher(Cipher.ENCRYPT_MODE);
        try {
            return coder.base64Encode(cipher.doFinal(property.getBytes(ENCODING)));
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            fail(byException(e) + "Failed to encrypt value ["+property+"]");
        }
        return null;
    }

    public String decrypt(String property) {
        initCipher(Cipher.DECRYPT_MODE);
        try {
            return new String(cipher.doFinal(coder.base64Decode(property)), ENCODING);
        } catch (IllegalBlockSizeException | BadPaddingException | IOException e) {
            fail(byException(e) + "Failed to decrypt value ["+property+"]");
        }
        return null;
    }

    private void initCipher(int cipherMode) {
        try {
            cipher.init(cipherMode, cipherKey, cipherParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            fail(byException(e) + "Failed to init Cipher in mode "+cipherMode);
        }
    }


}
