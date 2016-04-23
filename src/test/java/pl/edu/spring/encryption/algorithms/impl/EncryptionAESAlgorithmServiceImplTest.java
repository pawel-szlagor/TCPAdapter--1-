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
 * Created by Pawel on 2016-04-20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=CustomContextLoader.class, locations={"/META-INF/spring-config.xml"})
public class EncryptionAESAlgorithmServiceImplTest {
    @Autowired
    EncryptionAlgorithmService encryptionAESAlgorithmServiceImpl;

    @Test
    public void shouldEncryptSimpleWordWithoutPolishCharacters(){
        //given
        String originalMessage = "Hello world!";
        String key = "MZygpewJsCpRrfOr";
        EncryptionParameters parameters = new EncryptionParameters(EncryptionAlgorithms.AES, key);
        //when
        String encryptedMessage = encryptionAESAlgorithmServiceImpl.encryptStringMessage(originalMessage, parameters);
        //then
        assertEquals(encryptedMessage, "1aQVO6mXTEEr09+ZYirICA==");
    }
}