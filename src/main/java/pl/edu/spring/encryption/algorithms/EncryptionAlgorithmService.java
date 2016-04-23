package pl.edu.spring.encryption.algorithms;

import pl.edu.spring.encryption.EncryptionParameters;

/**
 * Created by Pawel on 2016-03-19.
 */
public interface EncryptionAlgorithmService {

    String encryptStringMessage(String originalMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException;
    String decryptStringMessage(String encryptedMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException;

    byte[] encryptByteMessage(byte[] originalMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException;
    byte[] decryptByteMessage(byte[] encryptedMessage, EncryptionParameters encryptionParameters) throws IllegalArgumentException;
}
