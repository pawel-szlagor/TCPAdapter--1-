package pl.edu.spring.encryption.algorithms;

/**
 * Created by Pawel on 2016-03-19.
 */
public interface EncryptionAlgorithmService {

    String encryptStringMessage(String originalMessage, String key) throws IllegalArgumentException;

    String decryptStringMessage(String encryptedMessage, String key) throws IllegalArgumentException;
}
