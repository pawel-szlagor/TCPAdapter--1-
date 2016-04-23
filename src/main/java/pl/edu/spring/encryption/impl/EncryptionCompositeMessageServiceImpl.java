package pl.edu.spring.encryption.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.spring.encryption.EncryptionCompositeMessageService;
import pl.edu.spring.encryption.EncryptionAlgorithms;
import pl.edu.spring.encryption.EncryptionParameters;
import pl.edu.spring.encryption.algorithms.EncryptionAlgorithmService;

import java.util.List;

/**
 * Created by Pawel on 2016-03-19.
 */
@Service
public class EncryptionCompositeMessageServiceImpl implements EncryptionCompositeMessageService {
    @Autowired
    private EncryptionAlgorithmService encryptionCaesarAlgorithmServiceImpl;
    @Autowired
    private EncryptionAlgorithmService encryptionROT13AlgorithmServiceImpl;
    @Autowired
    private EncryptionAlgorithmService encryptionVigenereAlgorithmServiceImpl;
    @Autowired
    private EncryptionAlgorithmService encryptionAESAlgorithmServiceImpl;

    @Override
    public List<EncryptionAlgorithms> getAllAvailableEncryptionAlgorithms() {
        return Lists.newArrayList(EncryptionAlgorithms.values());
    }

    @Override
    public String encryptStringMessage(String originalMessage, EncryptionParameters encParam) throws IllegalArgumentException {
        EncryptionAlgorithms algorithm = encParam.getAlgorithm();
        switch (algorithm) {
            case CAESAR:
                return encryptionCaesarAlgorithmServiceImpl.encryptStringMessage(originalMessage, encParam);
            case VIGENERE:
                return encryptionVigenereAlgorithmServiceImpl.encryptStringMessage(originalMessage, encParam);
            case ROT_13:
                return encryptionROT13AlgorithmServiceImpl.encryptStringMessage(originalMessage, encParam);
            case AES:
                return encryptionAESAlgorithmServiceImpl.encryptStringMessage(originalMessage, encParam);
            case NO_ALGORITHM:
                return originalMessage;
            default:
                throw new IllegalArgumentException("No implementation for choosen algortihm found");
        }
    }

    @Override
    public String decryptStringMessage(String encryptedMessage, EncryptionParameters encParam) {
        EncryptionAlgorithms algorithm = encParam.getAlgorithm();
        switch (algorithm) {
            case CAESAR:
                return encryptionCaesarAlgorithmServiceImpl.decryptStringMessage(encryptedMessage, encParam);
            case VIGENERE:
                return encryptionVigenereAlgorithmServiceImpl.decryptStringMessage(encryptedMessage, encParam);
            case ROT_13:
                return encryptionCaesarAlgorithmServiceImpl.decryptStringMessage(encryptedMessage, encParam);
            case AES:
                return encryptionAESAlgorithmServiceImpl.decryptStringMessage(encryptedMessage, encParam);
            case NO_ALGORITHM:
                return encryptedMessage;
            default:
                throw new IllegalArgumentException("No implementation for choosen algortihm found");
        }
    }

    @Override
    public byte[] encryptByteMessage(byte[] originalMessage, EncryptionParameters encParam) throws IllegalArgumentException {
        EncryptionAlgorithms algorithm = encParam.getAlgorithm();
        switch (algorithm) {
            case CAESAR:
                return encryptionCaesarAlgorithmServiceImpl.encryptByteMessage(originalMessage, encParam);
            case VIGENERE:
                return encryptionVigenereAlgorithmServiceImpl.encryptByteMessage(originalMessage, encParam);
            case ROT_13:
                return encryptionCaesarAlgorithmServiceImpl.encryptByteMessage(originalMessage, encParam);
            case AES:
                return encryptionAESAlgorithmServiceImpl.encryptByteMessage(originalMessage, encParam);
            case NO_ALGORITHM:
                return originalMessage;
            default:
                throw new IllegalArgumentException("No implementation for choosen algortihm found");
        }
    }

    @Override
    public byte[] decryptByteMessage(byte[] encryptedMessage, EncryptionParameters encParam) {
        EncryptionAlgorithms algorithm = encParam.getAlgorithm();
        switch (algorithm) {
            case CAESAR:
                return encryptionCaesarAlgorithmServiceImpl.decryptByteMessage(encryptedMessage, encParam);
            case VIGENERE:
                return encryptionVigenereAlgorithmServiceImpl.decryptByteMessage(encryptedMessage, encParam);
            case ROT_13:
                return encryptionCaesarAlgorithmServiceImpl.decryptByteMessage(encryptedMessage, encParam);
            case AES:
                return encryptionAESAlgorithmServiceImpl.decryptByteMessage(encryptedMessage, encParam);
            case NO_ALGORITHM:
                return encryptedMessage;
            default:
                throw new IllegalArgumentException("No implementation for choosen algortihm found");
        }
    }
}
