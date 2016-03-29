package pl.edu.spring.tcp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.serializer.DefaultDeserializer;
import org.springframework.core.serializer.DefaultSerializer;
import org.springframework.integration.annotation.*;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.ip.tcp.TcpInboundGateway;
import org.springframework.integration.ip.tcp.TcpOutboundGateway;
import org.springframework.integration.ip.tcp.connection.AbstractClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetClientConnectionFactory;
import org.springframework.integration.ip.tcp.connection.TcpNetServerConnectionFactory;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * Created by Pawel on 2016-03-12.
 */
@EnableIntegration
@IntegrationComponentScan
@Configuration
public class Config {

    @Autowired
    Environment environment;

    private int port = 3456;

    @MessagingGateway(defaultRequestChannel = "toTcp")
    public interface Gateway {
        String viaTcp(FileData in);
        String viaTcp(String in);
    }

    @Bean
    @ServiceActivator(inputChannel = "toTcp")
    public MessageHandler tcpOutGate(AbstractClientConnectionFactory connectionFactory) {
        TcpOutboundGateway gate = new TcpOutboundGateway();
        //connectionFactory.setSerializer(new CustomSerializerDeserializer());
        connectionFactory.setSerializer(new DefaultSerializer());
        gate.setConnectionFactory(connectionFactory);
        gate.setOutputChannelName("resultToString");
        return gate;
    }

    @Bean
    public TcpInboundGateway tcpInGate(AbstractServerConnectionFactory connectionFactory) {
        TcpInboundGateway inGate = new TcpInboundGateway();
        //connectionFactory.setDeserializer(new CustomSerializerDeserializer());
        connectionFactory.setDeserializer(new DefaultDeserializer());
        inGate.setConnectionFactory(connectionFactory);
        inGate.setRequestChannel(fromTcp());
        return inGate;
    }

    @Bean
    public MessageChannel fromTcp() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel toEcho() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel toTcp() {
        return new DirectChannel();
    }

    @MessageEndpoint
    public static class Echo {
        @Autowired
        TcpClientServerService tcpClientServerService;

        @Transformer(inputChannel = "fromTcp", outputChannel = "toEcho")
        public FileData convert(FileData fileReceived) {
            tcpClientServerService.receive(fileReceived);
            System.out.println("Otrzymano: " + fileReceived.getFileName());
            return fileReceived;
        }

        @Transformer(inputChannel = "fromTcp", outputChannel = "toEcho")
        public String convert(String stringReceived) {
            tcpClientServerService.receive(stringReceived);
            System.out.println("Otrzymano: " + stringReceived);
            return stringReceived;
        }

        @ServiceActivator(inputChannel = "toEcho")
        public String receiveFromEcho(FileData in) {
            tcpClientServerService.receive(in);
            System.out.println("Otrzymano: " + in.getFileName());
            return "Received file: " + in.getFileName();
        }

        @ServiceActivator(inputChannel = "toEcho")
        public String receiveFromEcho(String in) {
            System.out.println("Received textmessage: " + in);
            return in;
        }

        @Transformer(inputChannel = "resultToString")
        public String convertResponse(byte[] response) {
            return new String(response);
        }
    }

    @Bean
    public AbstractClientConnectionFactory clientCF() {
        return new TcpNetClientConnectionFactory(environment.getProperty("ipAddress"), Integer.parseInt(environment.getProperty("availableClientSocket")));
    }

    @Bean
    public AbstractServerConnectionFactory serverCF() {
        return new TcpNetServerConnectionFactory(Integer.parseInt(environment.getProperty("availableServerSocket")));
    }

}
