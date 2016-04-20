package pl.edu.spring.encryption;

import java.util.List;

/**
 * Created by Pawel on 2016-03-19.
 */
public interface EncryptionCompositeMessageService {
    List<EncryptionAlgorithms> getAllAvailableEncryptionAlgorithms();
    String encryptStringMessage(String originalMessage, EncryptionParameters encParam) throws IllegalArgumentException;
    String decryptStringMessage(String encryptedMessage, EncryptionParameters encParam) throws IllegalArgumentException;
    byte[] encryptByteMessage(byte[] originalMessage, EncryptionParameters encParam) throws IllegalArgumentException;
    byte[] decryptByteMessage(byte[] encryptedMessage, EncryptionParameters encParam) throws IllegalArgumentException;
}
