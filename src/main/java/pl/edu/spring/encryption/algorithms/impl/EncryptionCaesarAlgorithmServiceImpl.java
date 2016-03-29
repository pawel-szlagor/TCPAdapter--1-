package pl.edu.spring.encryption.algorithms.impl;

import org.springframework.stereotype.Service;
import pl.edu.spring.encryption.algorithms.EncryptionAlgorithmService;

/**
 * Created by Pawel on 2016-03-19.
 */
@Service
public class EncryptionCaesarAlgorithmServiceImpl implements EncryptionAlgorithmService {

    @Override
    public String encryptStringMessage(String originalMessage, String key) throws IllegalArgumentException{
        try {
            int shift = Integer.parseInt(key);
            return originalMessage.chars()
                    .map(character -> encryptSingleChar(character, shift))
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        }catch (NumberFormatException ex){
            throw new IllegalArgumentException("Podana wartość klucza nie jest w formacie liczbowym. Poprawna wartość to np. 13");
        }
    }

    @Override
    public String decryptStringMessage(String encryptedMessage, String key) {
        try {
            int shift = Integer.parseInt(key);
            return encryptedMessage.chars()
                    .map(character -> decryptSingleChar(character, shift))
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        }catch (NumberFormatException ex){
            throw new IllegalArgumentException("Podana wartość klucza nie jest w formacie liczbowym. Poprawna wartość to np. 13");
        }
    }

    private char decryptSingleChar(int character, int shift){
        return (char) (character < 'a' ? Math.floorMod(character -'A' - shift, 26) + 'A' : Math.floorMod(character - 'a' - shift, 26) + 'a');
    }

    private char encryptSingleChar(int character, int shift) {
        return (char) (character < 'a' ? Math.floorMod(character - 'A' + shift, 26) + 'A' : Math.floorMod(character - 'a' + shift, 26) + 'a');
    }

}
