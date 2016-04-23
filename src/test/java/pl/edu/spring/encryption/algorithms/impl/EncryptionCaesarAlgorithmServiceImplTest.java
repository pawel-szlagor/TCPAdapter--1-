package pl.edu.spring.encryption.algorithms.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.spring.encryption.EncryptionAlgorithms;
import pl.edu.spring.encryption.EncryptionParameters;
import pl.edu.spring.encryption.algorithms.EncryptionAlgorithmService;
import pl.edu.spring.tcp.support.CustomContextLoader;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pawel on 2016-03-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=CustomContextLoader.class, locations={"/META-INF/spring-config.xml"})
public class EncryptionCaesarAlgorithmServiceImplTest {

    @Autowired
    EncryptionAlgorithmService encryptionCaesarAlgorithmServiceImpl;

    @Test
    public void shouldEncryptSimpleWordWithoutPolishCharactersWithKeyOne(){
        //given
        String originalMessage = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String key = "1";
        EncryptionParameters parameters = getEncryptionParameters(key);
        //when
        String encryptedMessage = encryptionCaesarAlgorithmServiceImpl.encryptStringMessage(originalMessage, parameters);
        //then
        assertEquals(encryptedMessage, "bcdefghijklmnopqrstuvwxyzaBCDEFGHIJKLMNOPQRSTUVWXYZA");
    }

    @Test
    public void shouldEncryptSimpleWordWithoutPolishCharactersWithKeyEqual26(){
        //given
        String originalMessage = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String key = "26";
        EncryptionParameters parameters = getEncryptionParameters(key);
        //when
        String encryptedMessage = encryptionCaesarAlgorithmServiceImpl.encryptStringMessage(originalMessage, parameters);
        //then
        assertEquals(encryptedMessage, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    @Test
    public void shouldEncryptSimpleWordWithoutPolishCharactersWithKeyBiggerThan26(){
        //given
        String originalMessage = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String key = "27";
        EncryptionParameters parameters = getEncryptionParameters(key);
        //when
        String encryptedMessage = encryptionCaesarAlgorithmServiceImpl.encryptStringMessage(originalMessage, parameters);
        //then
        assertEquals(encryptedMessage, "bcdefghijklmnopqrstuvwxyzaBCDEFGHIJKLMNOPQRSTUVWXYZA");
    }

    @Test
    public void shouldDecryptSimpleWordWithoutPolishCharactersWithKeyOne(){
        //given
        String encryptedMessage = "bcdefghijklmnopqrstuvwxyzaBCDEFGHIJKLMNOPQRSTUVWXYZA";
        String key = "1";
        EncryptionParameters parameters = getEncryptionParameters(key);
        //when
        String decryptedMessage = encryptionCaesarAlgorithmServiceImpl.decryptStringMessage(encryptedMessage, parameters);
        //then
        assertEquals(decryptedMessage, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    @Test
    public void shouldDecryptSimpleWordWithoutPolishCharactersWithKeyEqual26(){
        //given
        String encryptedMessage = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String key = "26";
        EncryptionParameters parameters = getEncryptionParameters(key);
        //when
        String decryptedMessage = encryptionCaesarAlgorithmServiceImpl.decryptStringMessage(encryptedMessage, parameters);
        //then
        assertEquals(decryptedMessage, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    @Test
    public void shouldDecryptSimpleWordWithoutPolishCharactersWithKeyBiggerThan26(){
        //given
        String encryptedMessage = "bcdefghijklmnopqrstuvwxyzaBCDEFGHIJKLMNOPQRSTUVWXYZA";
        String key = "27";
        EncryptionParameters parameters = getEncryptionParameters(key);
        //when
        String decryptedMessage = encryptionCaesarAlgorithmServiceImpl.decryptStringMessage(encryptedMessage, parameters);
        //then
        assertEquals(decryptedMessage, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    private EncryptionParameters getEncryptionParameters(String key) {
        return new EncryptionParameters(EncryptionAlgorithms.CAESAR, key);
    }
}