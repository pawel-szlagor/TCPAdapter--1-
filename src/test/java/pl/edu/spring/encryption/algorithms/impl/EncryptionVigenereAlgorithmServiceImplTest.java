package pl.edu.spring.encryption.algorithms.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.edu.spring.encryption.algorithms.EncryptionAlgorithmService;
import pl.edu.spring.tcp.support.CustomContextLoader;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pawel on 2016-03-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=CustomContextLoader.class, locations={"/META-INF/spring-config.xml"})
public class EncryptionVigenereAlgorithmServiceImplTest {

    @Qualifier("encryptionVigenereAlgorithmServiceImpl")
    @Autowired
    EncryptionAlgorithmService encryptionVigenereAlgorithmService;

    @Test
    public void shouldEncryptSimpleWordWithoutPolishCharactersWithKeyOne(){
        //given
        String originalMessage = "TO JEST BARDZO TAJNY TEKST";
        String key = "TAJNE";
        //when
        String encryptedMessage = encryptionVigenereAlgorithmService.encryptStringMessage(originalMessage, key);
        //then
        assertEquals(encryptedMessage, "MO SRWM BJEHSO CNNGY CROLT");
    }

    @Test
    public void shouldDecryptSimpleWordWithoutPolishCharactersWithKeyEqual26(){
        //given
        String encryptedMessage = "MO SRWM BJEHSO CNNGY CROLT";
        String key = "TAJNE";
        //when
        String decryptedMessage = encryptionVigenereAlgorithmService.decryptStringMessage(encryptedMessage, key);
        //then
        assertEquals(decryptedMessage, "TO JEST BARDZO TAJNY TEKST");
    }
}