package pl.edu.spring.encryption.algorithms.impl;

import org.apache.commons.net.util.Base64;
import org.springframework.stereotype.Service;
import pl.edu.spring.encryption.algorithms.EncryptionAlgorithmService;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Pawel on 2016-03-19.
 */
@Service
public class EncryptionAESAlgorithmServiceImpl implements EncryptionAlgorithmService {

    @Override
    public String encryptStringMessage(String originalMessage, String key) throws IllegalArgumentException {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(originalMessage.getBytes());
            return Base64.encodeBase64String(encrypted).trim();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public String decryptStringMessage(String encryptedMessage, String key) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encryptedMessage));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] encryptByteMessage(byte[] originalMessage, String key) throws IllegalArgumentException {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            return cipher.doFinal(originalMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decryptByteMessage(byte[] encryptedMessage, String key) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            return cipher.doFinal(encryptedMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
