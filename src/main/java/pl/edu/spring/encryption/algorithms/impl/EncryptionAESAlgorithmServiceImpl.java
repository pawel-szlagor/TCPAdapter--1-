package pl.edu.spring.encryption.algorithms.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.util.Base64;
import org.springframework.stereotype.Service;
import pl.edu.spring.encryption.EncryptionParameters;
import pl.edu.spring.encryption.algorithms.EncryptionAlgorithmService;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Created by Pawel on 2016-03-19.
 */
@Service
public class EncryptionAESAlgorithmServiceImpl implements EncryptionAlgorithmService {

    @Override
    public String encryptStringMessage(String originalMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException {
        try {
            Cipher cipher = initializeEncryptCipher(encryptionParameters);
            byte[] encrypted = cipher.doFinal(originalMessage.getBytes());
            return Base64.encodeBase64String(encrypted).trim();
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @Override
    public String decryptStringMessage(String encryptedMessage, EncryptionParameters encryptionParameters) {
        try {
            Cipher cipher = initializeDecryptCipher(encryptionParameters);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encryptedMessage));
            return new String(original);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @Override
    public byte[] encryptByteMessage(byte[] originalMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException {
        try {
            Cipher cipher = initializeEncryptCipher(encryptionParameters);
            return cipher.doFinal(originalMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public byte[] decryptByteMessage(byte[] encryptedMessage, EncryptionParameters encryptionParameters) {
        try {
            Cipher cipher = initializeDecryptCipher(encryptionParameters);
            return cipher.doFinal(encryptedMessage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Cipher initializeEncryptCipher(EncryptionParameters encryptionParameters) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String key = getKeyFromParameters(encryptionParameters);
        String initVector = getInitVectorFromParameters(encryptionParameters);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher;
        if(StringUtils.isBlank(initVector)){
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        } else{
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        }
        return cipher;
    }

    private Cipher initializeDecryptCipher(EncryptionParameters encryptionParameters) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        String key = getKeyFromParameters(encryptionParameters);
        String initVector = getInitVectorFromParameters(encryptionParameters);
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher;
        if(StringUtils.isBlank(initVector)){
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        } else{
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        }
        return cipher;
    }

    private String getKeyFromParameters(EncryptionParameters encryptionParameters) {
        String key = encryptionParameters.getStringKey();
        if(StringUtils.isBlank(key)){
            File fileWithKeyToRead = encryptionParameters.getFileKey();
            try {
                Scanner scanner = new Scanner(fileWithKeyToRead);
                return scanner.nextLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }else{
            return key;
        }
    }

    private String getInitVectorFromParameters(EncryptionParameters encryptionParameters) {
        String initVector = encryptionParameters.getInitVector();
        File fileWithInitVectorToRead = encryptionParameters.getFileKey();
        if(StringUtils.isBlank(initVector) && fileWithInitVectorToRead != null){
            try {
                Scanner scanner = new Scanner(fileWithInitVectorToRead);
                scanner.nextLine();
                return scanner.hasNextLine() ? scanner.nextLine() : "";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return "";
        }else{
            return initVector;
        }
    }

}
