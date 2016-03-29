package pl.edu.spring.application.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.integration.test.util.SocketUtils;
import org.springframework.stereotype.Controller;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import pl.edu.spring.application.StartMessageApplication;
import pl.edu.spring.encryption.EncryptionAlgorithms;
import pl.edu.spring.encryption.EncryptionCompositeMessageService;
import pl.edu.spring.encryption.EncryptionParameters;
import pl.edu.spring.encryption.impl.EncryptionCompositeMessageServiceImpl;
import pl.edu.spring.tcp.FileData;
import pl.edu.spring.tcp.TcpClientServerService;
import pl.edu.spring.tcp.support.CustomContextLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static pl.edu.spring.encryption.EncryptionAlgorithms.NO_ALGORITHM;
import static pl.edu.spring.encryption.EncryptionAlgorithms.values;

@Controller
@ContextConfiguration(loader = CustomContextLoader.class, locations = "/META-INF/spring-config.xml")
@DirtiesContext
public class MessageFrameController {
    public static final String MESSAGE_SENDING_IN_PROGRESS = "trwa wysyłanie wiadomości...";
    @FXML
    private Button sendButton;
    @FXML
    private Button closeButton;
    @FXML
    private TextArea textInput;
    @FXML
    private TextArea textOutput;
    @FXML
    private Button sendFileButton;
    @FXML
    private ComboBox<EncryptionAlgorithms> algorithmComboBox;
    @FXML
    private PasswordField keyPasswordField;
    @FXML
    private Button chooseDirectoryButton;
    @FXML
    private Button decryptButton;
    @FXML
    private Button chooseKeyFileButton;

    @Autowired
    private TcpClientServerService tcpClientServerService;
    @Autowired
    private EncryptionCompositeMessageService encryptionCompositeMessageService;
    private String ipAddress;
    private String hostPort;
    private String clientPort;
    private StartMessageApplication application;
    private static final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss:SSS");
    private static final String WELCOMETEXT
            = "\n========================================================="
            + "\n                                                         "
            + "\n       Witaj w komunikatorze szyfrującym                 "
            + "\n          z użyciem TCP-Client-Server                    "
            + "\n                                                         "
            + "\n=========================================================";
    private GenericXmlApplicationContext context;
    private File selectedFileToSend;
    private File directoryForReceivedFiles = new File("E:\\Pobrane");
    private String lastReceivedMessage;

    public MessageFrameController() {
    }

    @FXML
    public void initialize(){
        initializeTextOutput();
        initializeEncryptionOptions();
        activateControls();
    }

    public void initializeContext() {
        context = new GenericXmlApplicationContext();
        System.out.print("Detect open server socket...");
        int availableServerSocket = SocketUtils.findAvailableServerSocket(Integer.parseInt(hostPort));
        int availableClientSocket = SocketUtils.findAvailableServerSocket(Integer.parseInt(clientPort));

        final Map<String, Object> sockets = new HashMap<String, Object>();
        sockets.put("availableServerSocket", availableServerSocket);
        sockets.put("availableClientSocket", availableClientSocket);

        sockets.put("ipAddress", ipAddress);

        final MapPropertySource propertySource = new MapPropertySource("sockets", sockets);

        context.getEnvironment().getPropertySources().addLast(propertySource);
        context.load("classpath:META-INF/spring-config.xml");
        context.registerShutdownHook();
        context.refresh();
        System.out.print("Connection established succesful with server ip " + ipAddress + " and using hostPort " + hostPort + " and using clientPort " + clientPort);
        tcpClientServerService = context.getBean(TcpClientServerService.class);
        encryptionCompositeMessageService = context.getBean(EncryptionCompositeMessageServiceImpl.class);
        tcpClientServerService.setController(this);
    }

    private void initializeEncryptionOptions() {
        algorithmComboBox.getItems().addAll(values());
        algorithmComboBox.setValue(NO_ALGORITHM);
        algorithmComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(EncryptionAlgorithms.NO_ALGORITHM)){
                keyPasswordField.setDisable(true);
                chooseKeyFileButton.setDisable(true);
            } else {
                keyPasswordField.setDisable(true);
                chooseKeyFileButton.setDisable(true);
            }
        });
    }

    private void initializeTextOutput() {
        textOutput.setText(WELCOMETEXT);
        textOutput.positionCaret(textOutput.getLength());
        textOutput.textProperty().addListener((observable, oldValue, newValue) -> {
            textOutput.positionCaret(textOutput.getLength()); //this will scroll to the bottom
            //use Double.MIN_VALUE to scroll to the top
        });
    }

    public void setApplication(StartMessageApplication application) {
        this.application = application;
    }

    private void activateControls() {
        textInput.setDisable(false);
        textOutput.setDisable(false);
        sendFileButton.setDisable(false);
        chooseDirectoryButton.setDisable(false);
        sendFileButton.setDisable(false);
        sendButton.setDisable(false);
        decryptButton.setDisable(false);
        algorithmComboBox.setDisable(false);
        keyPasswordField.setDisable(false);
    }

    //JAVA FX ACTIONS

    public void send() {
        if (selectedFileToSend != null && textInput.getText().equals("Wybrano plik: " + selectedFileToSend.getAbsolutePath() + ". Czy na pewno chcesz wysłać?")) {
            sendFile();
        } else {
            sendMessage();
        }
    }

    private void sendFile() {
        Task sendingTask = new Task() {
            @Override
            protected Object call() throws Exception {
                String answer = tcpClientServerService.sendFile(selectedFileToSend);
                String output = textOutput.getText();
                output = output.concat("\n" +"other  " + df.format(new Date()) + "\n" +answer);
                textOutput.setText(output);
                return null;
            }
        };
        new Thread(sendingTask).start();
    }

    private void sendMessage() {
        Task sendingTask = new Task() {
            @Override
            protected Object call() throws IOException {
                addMessageInProgressInOutput();
                String messageToSend = textInput.getText();
                EncryptionParameters encParams = getEncParams();
                String encryptedMessage = encryptionCompositeMessageService.encryptStringMessage(messageToSend, encParams);
                String answer = tcpClientServerService.sendTextMessage(encryptedMessage);
                textInput.clear();
                String output = textOutput.getText();
                textOutput.setText(output.replace(MESSAGE_SENDING_IN_PROGRESS, "me:" + df.format(new Date()) + "\n" + messageToSend));
                return null;
            }
        };
        Thread sendingMessageThread = new Thread(sendingTask);
        sendingMessageThread.run();
        try {
            sendingTask.get();
        }catch (IllegalArgumentException ex){
            handleSendingError(ex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        keyPasswordField.clear();
    }

    private void handleSendingError(Exception ex) {
        Alert exceptionAlert = new Alert(Alert.AlertType.ERROR, ex.getCause().getMessage(), ButtonType.CLOSE);
        exceptionAlert.setHeaderText("Podano niepoprawne dane!");
        exceptionAlert.setTitle("Błąd - niepoprawne dane!");
        exceptionAlert.show();
    }

    private EncryptionParameters getEncParams() {
        EncryptionAlgorithms algorithmChosen = algorithmComboBox.getValue();
        switch(algorithmChosen){
            case NO_ALGORITHM:
            case CAESAR:
            case ROT_13:
                return new EncryptionParameters(algorithmChosen, keyPasswordField.getText());
        }
        return null;
    }

    private void addMessageInProgressInOutput() {
        String output = textOutput.getText();
        if(output.equals(WELCOMETEXT)){
            output = "";
        }
        output = output.concat("\n" + MESSAGE_SENDING_IN_PROGRESS);
        textOutput.setText(output);
    }

    public void receiveMessage(String input) {
        lastReceivedMessage = input;
        String output = textOutput.getText();
        if(output.equals(WELCOMETEXT)){
            output = "";
        }
        output = output.concat("\n" +"other " + df.format(new Date()) + "\n"  + input);
        textOutput.setText(output);
    }

    public void decrypt(){
        String decryptedMessage = encryptionCompositeMessageService.decryptStringMessage(lastReceivedMessage, getEncParams());
        String output = textOutput.getText();
        output = output.concat("\n" +"po deszyfryzacji: " + df.format(new Date()) + "\n"  + decryptedMessage);
        textOutput.setText(output);
    }

    public void chooseFileToSend(){
        FileChooser fileChooser = new FileChooser();
        selectedFileToSend = fileChooser.showOpenDialog(null);
        if(selectedFileToSend != null){
            textInput.setText("Wybrano plik: " + selectedFileToSend.getAbsolutePath() + ". Czy na pewno chcesz wysłać?");
        }
    }

    public void chooseKeyFile(){
        //noop
    }

    public void chooseDirectory(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryForReceivedFiles = directoryChooser.showDialog(null);
    }

    public void closeApplication() throws Exception {
        application.stop();

    }

    public void receiveMessage(FileData in) {
        String output = textOutput.getText();
        if(output.equals(WELCOMETEXT)){
            output = "";
        }
        File fileToWrite = new File(directoryForReceivedFiles.getAbsolutePath() + "\\" + in.getFileName());
        try {
            FileOutputStream fos = new FileOutputStream(fileToWrite);
            fos.write(in.getByteArray());
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        output = output.concat("\n" +"other " + df.format(new Date()) + "\n"  + "otrzymano: "+ in.getFileName());
        textOutput.setText(output);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getHostPort() {
        return hostPort;
    }

    public void setHostPort(String hostPort) {
        this.hostPort = hostPort;
    }

    public String getClientPort() {
        return clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }
}
