package pl.edu.spring.encryption.algorithms.impl;

import org.springframework.stereotype.Service;
import pl.edu.spring.encryption.EncryptionParameters;
import pl.edu.spring.encryption.algorithms.EncryptionAlgorithmService;

/**
 * Created by Pawel on 2016-03-19.
 */
@Service
public class EncryptionVigenereAlgorithmServiceImpl implements EncryptionAlgorithmService {

    @Override
    public String encryptStringMessage(String originalMessage, EncryptionParameters encryptionParameters) {
        String key = encryptionParameters.getStringKey();
        StringBuilder builder = new StringBuilder();
        int keyLength = key.length();
        for (int i = 0, j = 0; i < originalMessage.length(); i++) {
            char character = originalMessage.charAt(i);
            if (Character.isWhitespace(character)) {
                builder.append(character);
            } else {
                builder.append(encryptSingleChar(originalMessage.charAt(i), key.charAt(Math.floorMod(j, keyLength))));
                j++;
            }
        }
        return builder.toString();
    }

    @Override
    public String decryptStringMessage(String encryptedMessage, EncryptionParameters encryptionParameters) {
        String key = encryptionParameters.getStringKey();
        StringBuilder builder = new StringBuilder();
        int keyLength = key.length();
        for (int i = 0, j = 0; i < encryptedMessage.length(); i++) {
            char character = encryptedMessage.charAt(i);
            if (Character.isWhitespace(character)) {
                builder.append(character);
            } else {
                builder.append(decryptSingleChar(encryptedMessage.charAt(i), key.charAt(Math.floorMod(j, keyLength))));
                j++;
            }
        }
        return builder.toString();
    }

    @Override
    public byte[] encryptByteMessage(byte[] originalMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException {
        String key = encryptionParameters.getStringKey();
        byte[] result = new byte[originalMessage.length];
        int keyLength = key.length();
        for (int i = 0; i < originalMessage.length; i++) {
            result[i] = (byte) encryptSingleByte(originalMessage[i], key.charAt(Math.floorMod(i, keyLength)));
        }
        return result;
    }

    @Override
    public byte[] decryptByteMessage(byte[] encryptedMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException {
        String key = encryptionParameters.getStringKey();
        byte[] result = new byte[encryptedMessage.length];
        int keyLength = key.length();
        for (int i = 0; i < encryptedMessage.length; i++) {
            result[i] = (byte) decryptSingleByte(encryptedMessage[i], key.charAt(Math.floorMod(i, keyLength)));
        }
        return result;
    }

    private char decryptSingleChar(int character, int shift) {
        return (character <= 'Z' && character >= 'A') ?
                (char) (Math.floorMod(character - 'A' - shift + 'A', 26) + 'A') :
                (char) (Math.floorMod(character - 'a' - shift + 'a', 26) + 'a');
    }

    private char encryptSingleChar(int character, int shift) {
        return (character <= 'Z' && character >= 'A') ?
                (char) (Math.floorMod(character - 'A' + shift - 'A', 26) + 'A') :
                (char) (Math.floorMod(character - 'a' + shift - 'a', 26) + 'a');
    }

    private int encryptSingleByte(int i, int shift) {
        return (i + shift + 128) % 256 - 128;
    }

    private int decryptSingleByte(int i, int shift) {
        return (i + 128 - shift) % 256 - 128;
    }

}
