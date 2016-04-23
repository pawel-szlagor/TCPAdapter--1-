package pl.edu.spring.application.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import pl.edu.spring.application.ConnectionParams;
import pl.edu.spring.application.StartMessageApplication;
import pl.edu.spring.encryption.EncryptionAlgorithms;
import pl.edu.spring.encryption.EncryptionCompositeMessageService;
import pl.edu.spring.encryption.EncryptionParameters;
import pl.edu.spring.encryption.impl.EncryptionCompositeMessageServiceImpl;
import pl.edu.spring.tcp.FileData;
import pl.edu.spring.tcp.TcpClientServerService;
import pl.edu.spring.tcp.support.CustomContextLoader;

import java.io.*;
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
    private PasswordField initVecPasswordField;
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
    private ConnectionParams connectionParams;
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
    private File receivedFile;
    private File directoryForReceivedFiles = new File("E:\\Pobrane");
    private String lastReceivedMessage;
    private File keyFile;

    public MessageFrameController() {
    }

    @FXML
    public void initialize(){
        initializeTextOutput();
        activateControls();
        initializeEncryptionOptions();
    }

    public void initializeContext() {
        context = new GenericXmlApplicationContext();
        System.out.print("Detect open server socket...");
        int serverSocket = Integer.parseInt(connectionParams.getHostPort());
        int clientSocket = Integer.parseInt(connectionParams.getClientPort());

        final Map<String, Object> sockets = new HashMap<String, Object>();
        sockets.put("availableServerSocket", serverSocket);
        sockets.put("availableClientSocket", clientSocket);

        sockets.put("ipAddress", connectionParams.getIpAddress());

        final MapPropertySource propertySource = new MapPropertySource("sockets", sockets);

        context.getEnvironment().getPropertySources().addLast(propertySource);
        context.load("classpath:META-INF/spring-config.xml");
        context.registerShutdownHook();
        context.refresh();
        System.out.print("Connection established succesful with server ip " + connectionParams.getIpAddress() + " and using hostPort " + connectionParams.getHostPort() + " and using clientPort " + connectionParams.getClientPort());
        tcpClientServerService = context.getBean(TcpClientServerService.class);
        encryptionCompositeMessageService = context.getBean(EncryptionCompositeMessageServiceImpl.class);
        tcpClientServerService.setController(this);
    }

    private void initializeEncryptionOptions() {
        algorithmComboBox.getItems().addAll(values());
        algorithmComboBox.setValue(NO_ALGORITHM);
        algorithmComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue){
                case NO_ALGORITHM:
                case ROT_13:
                    keyPasswordField.setDisable(true);
                    initVecPasswordField.setDisable(true);
                    chooseKeyFileButton.setDisable(true);
                    break;
                case CAESAR:
                case VIGENERE:
                    keyPasswordField.setDisable(false);
                    break;
                case AES:
                    keyPasswordField.setDisable(false);
                    chooseKeyFileButton.setDisable(false);
                    initVecPasswordField.setDisable(false);
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
        sendButton.setDisable(false);
        decryptButton.setDisable(false);
        algorithmComboBox.setDisable(false);
        keyPasswordField.setDisable(true);
        initVecPasswordField.setDisable(true);
        chooseKeyFileButton.setDisable(true);
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
                File fileEncrypted = new File(selectedFileToSend.getPath()+ (2));
                FileOutputStream fos = new FileOutputStream(fileEncrypted);
                InputStream in = new FileInputStream(selectedFileToSend);
                byte[] bytes = new byte[(int) selectedFileToSend.length()];
                in.read(bytes);
                fos.write(encryptionCompositeMessageService.encryptByteMessage(bytes, getEncParams()));
                fos.close();
                in.close();
                String answer = tcpClientServerService.sendFile(fileEncrypted);
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
    }

    private void handleSendingError(Exception ex) {
        Alert exceptionAlert = new Alert(Alert.AlertType.ERROR, ex.getCause().getMessage(), ButtonType.CLOSE);
        exceptionAlert.setHeaderText("Podano niepoprawne dane!");
        exceptionAlert.setContentText(ex.getMessage());
        exceptionAlert.setTitle("Błąd - niepoprawne dane!");
        exceptionAlert.show();
    }

    private EncryptionParameters getEncParams() {
        EncryptionAlgorithms algorithmChosen = algorithmComboBox.getValue();
        switch(algorithmChosen){
            case NO_ALGORITHM:
            case CAESAR:
            case ROT_13:
            case VIGENERE:
                return new EncryptionParameters(algorithmChosen, keyPasswordField.getText());
            case AES:
                return new EncryptionParameters(algorithmChosen, keyPasswordField.getText(), initVecPasswordField.getText(), keyFile);
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
        if(receivedFile != null){
            decryptReceivedFile();
        }else{
            decryptStringMessage();
        }
    }

    private void decryptReceivedFile() {
        File decryptedFile = new File(directoryForReceivedFiles.getAbsolutePath() + "\\" + receivedFile.getName().substring(0, receivedFile.getName().length()-1));
        try {
            FileOutputStream fos = new FileOutputStream(decryptedFile);
            InputStream in = new FileInputStream(receivedFile);
            byte[] bytes = new byte[(int) receivedFile.length()];
            in.read(bytes);
            fos.write(encryptionCompositeMessageService.decryptByteMessage(bytes, getEncParams()));
            fos.close();
            in.close();
            receivedFile = null;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void decryptStringMessage() {
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
        FileChooser fileChooser = new FileChooser();
        keyFile = fileChooser.showOpenDialog(null);
        System.out.println("wybrano plik z kluczem: " + keyFile.getAbsolutePath());
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
        receivedFile = new File(directoryForReceivedFiles.getAbsolutePath() + "\\" + in.getFileName());
        try {
            FileOutputStream fos = new FileOutputStream(receivedFile);
            fos.write(in.getByteArray());
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        output = output.concat("\n" +"other " + df.format(new Date()) + "\n"  + "otrzymano: "+ in.getFileName());
        textOutput.setText(output);
    }

    public ConnectionParams getConnectionParams() {
        return connectionParams;
    }

    public void setConnectionParams(ConnectionParams connectionParams) {
        this.connectionParams = connectionParams;
    }
}
