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

    @Override
    public List<EncryptionAlgorithms> getAllAvailableEncryptionAlgorithms() {
        return Lists.newArrayList(EncryptionAlgorithms.values());
    }

    @Override
    public String encryptStringMessage(String originalMessage, EncryptionParameters encParam) throws IllegalArgumentException{
        EncryptionAlgorithms algorithm = encParam.getAlgorithm();
        String key = encParam.getKey();
        switch(algorithm){
            case CAESAR: return encryptionCaesarAlgorithmServiceImpl.encryptStringMessage(originalMessage, key);
            case NO_ALGORITHM: return originalMessage;
            default: throw new IllegalArgumentException("No implementation for choosen algortihm found");
        }
    }

    @Override
    public String decryptStringMessage(String encryptedMessage, EncryptionParameters encParam) {
        EncryptionAlgorithms algorithm = encParam.getAlgorithm();
        String key = encParam.getKey();
        switch(algorithm){
            case CAESAR: return encryptionCaesarAlgorithmServiceImpl.decryptStringMessage(encryptedMessage, key);
            case NO_ALGORITHM: return encryptedMessage;
            default: throw new IllegalArgumentException("No implementation for choosen algortihm found");
        }
    }
}
