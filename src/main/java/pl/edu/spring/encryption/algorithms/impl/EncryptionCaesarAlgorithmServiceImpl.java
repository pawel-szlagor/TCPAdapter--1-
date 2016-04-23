package pl.edu.spring.encryption.algorithms.impl;

import org.springframework.stereotype.Service;
import pl.edu.spring.encryption.EncryptionParameters;
import pl.edu.spring.encryption.algorithms.EncryptionAlgorithmService;

import java.util.stream.IntStream;

/**
 * Created by Pawel on 2016-03-19.
 */
@Service
public class EncryptionCaesarAlgorithmServiceImpl implements EncryptionAlgorithmService {

    @Override
    public String encryptStringMessage(String originalMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException {
        try {
            String key = encryptionParameters.getStringKey();
            int shift = Integer.parseInt(key);
            return originalMessage.chars()
                    .map(character -> encryptSingleChar(character, shift))
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Podana wartość klucza nie jest w formacie liczbowym. Poprawna wartość to np. 13");
        }
    }

    @Override
    public String decryptStringMessage(String encryptedMessage, EncryptionParameters encryptionParameters) {
        try {
            String key = encryptionParameters.getStringKey();
            int shift = Integer.parseInt(key);
            return encryptedMessage.chars()
                    .map(character -> decryptSingleChar(character, shift))
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Podana wartość klucza nie jest w formacie liczbowym. Poprawna wartość to np. 13");
        }
    }

    private char decryptSingleChar(int character, int shift) {
        return (char) (character < 'a' ? Math.floorMod(character - 'A' - shift, 26) + 'A' : Math.floorMod(character - 'a' - shift, 26) + 'a');
    }

    private char encryptSingleChar(int character, int shift) {
        return (char) (character < 'a' ? Math.floorMod(character - 'A' + shift, 26) + 'A' : Math.floorMod(character - 'a' + shift, 26) + 'a');
    }

    @Override
    public byte[] encryptByteMessage(byte[] originalMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException {
        try {
            String key = encryptionParameters.getStringKey();
            int shift = Integer.parseInt(key);
            int[] resultInt = IntStream.range(0, originalMessage.length).map(i -> encyptSingleByte(originalMessage[i], shift)).toArray();
            byte[] resultByte = new byte[resultInt.length];
            for (int i = 0; i < resultInt.length; i++) {
                resultByte[i] = (byte) resultInt[i];
            }
            return resultByte;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Podana wartość klucza nie jest w formacie liczbowym. Poprawna wartość to np. 13");
        }
    }

    @Override
    public byte[] decryptByteMessage(byte[] encryptedMessage, EncryptionParameters encryptionParameters) {
        try {
            String key = encryptionParameters.getStringKey();
            int shift = Integer.parseInt(key);
            int[] resultInt = IntStream.range(0, encryptedMessage.length).map(i -> decyptSingleByte(encryptedMessage[i], shift)).toArray();
            byte[] resultByte = new byte[resultInt.length];
            for (int i = 0; i < resultInt.length; i++) {
                resultByte[i] = (byte) resultInt[i];
            }
            return resultByte;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Podana wartość klucza nie jest w formacie liczbowym. Poprawna wartość to np. 13");
        }
    }

    private int encyptSingleByte(int i, int shift) {
        return (i + shift + 128) % 256 - 128;
    }

    private int decyptSingleByte(int i, int shift) {
        return (i + 128 - shift) % 256 - 128;
    }

}
