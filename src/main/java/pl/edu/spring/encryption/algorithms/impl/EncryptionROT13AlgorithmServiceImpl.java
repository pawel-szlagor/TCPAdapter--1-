package pl.edu.spring.encryption.algorithms.impl;

import org.springframework.stereotype.Service;
import pl.edu.spring.encryption.EncryptionParameters;
import pl.edu.spring.encryption.algorithms.EncryptionAlgorithmService;

import java.util.stream.IntStream;

/**
 * Created by Pawel on 2016-03-19.
 */
@Service
public class EncryptionROT13AlgorithmServiceImpl implements EncryptionAlgorithmService {

    @Override
    public String encryptStringMessage(String originalMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException {
        try {
            return originalMessage.chars()
                    .map(character -> encryptSingleChar(character))
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
                    .map(character -> decryptSingleChar(character))
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Podana wartość klucza nie jest w formacie liczbowym. Poprawna wartość to np. 13");
        }
    }

    private char decryptSingleChar(int character) {
        return (char) (character < 'a' ? Math.floorMod(character - 'A' - 13, 26) + 'A' : Math.floorMod(character - 'a' - 13, 26) + 'a');
    }

    private char encryptSingleChar(int character) {
        return (char) (character < 'a' ? Math.floorMod(character - 'A' + 13, 26) + 'A' : Math.floorMod(character - 'a' + 13, 26) + 'a');
    }

    @Override
    public byte[] encryptByteMessage(byte[] originalMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException {
        try {
            int[] resultInt = IntStream.range(0, originalMessage.length).map(i -> encyptSingleByte(originalMessage[i])).toArray();
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
            int[] resultInt = IntStream.range(0, encryptedMessage.length).map(i -> decyptSingleByte(encryptedMessage[i])).toArray();
            byte[] resultByte = new byte[resultInt.length];
            for (int i = 0; i < resultInt.length; i++) {
                resultByte[i] = (byte) resultInt[i];
            }
            return resultByte;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Podana wartość klucza nie jest w formacie liczbowym. Poprawna wartość to np. 13");
        }
    }

    private int encyptSingleByte(int i) {
        return (i + 13 + 128) % 256 - 128;
    }

    private int decyptSingleByte(int i) {
        return (i + 128 - 13) % 256 - 128;
    }

}
