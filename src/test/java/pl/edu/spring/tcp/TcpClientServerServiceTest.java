package pl.edu.spring.tcp;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import pl.edu.spring.tcp.support.CustomContextLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by Pawel on 2016-03-12.
 */
@ContextConfiguration(loader=CustomContextLoader.class, locations={"/META-INF/spring-config.xml"})
@DirtiesContext
public class TcpClientServerServiceTest {

    @Autowired
    private TcpClientServerService tcpClientServerService;

    @Test
    public void shouldSendTextMessage() throws IOException {
        //given
        String message = "message";
        //when
        String response = tcpClientServerService.sendTextMessage(message);
        //then
        assertEquals("message", response);
    }

    @Test
    public void shouldSendFile() throws IOException {
        //given
        File fileToSend = new File("F:\\image.jpg");
        InputStream in = new FileInputStream(fileToSend);
        byte [] bytes = new byte[(int)fileToSend.length()];
        FileData fileData = new FileData(bytes, fileToSend.getName());
        //when
        String result = tcpClientServerService.sendFile(fileToSend);
        //then
        assertEquals("F:\\image.jpg", result);
    }
}