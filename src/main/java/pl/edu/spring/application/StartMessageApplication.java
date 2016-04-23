package pl.edu.spring.application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.test.context.ContextConfiguration;
import pl.edu.spring.application.controller.ConnectionFrameController;
import pl.edu.spring.application.controller.MessageFrameController;
import pl.edu.spring.tcp.Config;

import java.io.IOException;
import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static javafx.scene.control.ButtonType.CANCEL;

@ContextConfiguration(classes = Config.class)
public class StartMessageApplication extends Application {

    private MessageFrameController controller;
    private ConnectionFrameController connectionController;
    private String ipAddress;
    private String hostPort;
    private Stage primaryStage;
    private Alert waitingDialog;
    private String clientPort;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
       /* FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/ConnectionFrame.fxml"));
        GridPane connectionLayout = loader.load();
        connectionController = loader.getController();
        connectionController.setApplication(this);
        primaryStage.setTitle("Choose connection options");
        Scene connectionScene = new Scene(connectionLayout);
        primaryStage.setScene(connectionScene);
        primaryStage.show();*/
        Dialog<ConnectionParams> dialog = new Dialog<>();
        dialog.setTitle("Wybierz parametry połączenia");
        dialog.setHeaderText("Proszę wprowadzić adres IP hosta oraz porty użyte do połączenia.");
        dialog.setResizable(true);

        Label label1 = new Label("Adres IP Hosta: ");
        Label label2 = new Label("Wykorzystany hostPort hosta: ");
        Label label3 = new Label("Wykorzystany hostPort klienta: ");
        TextField text1 = new TextField();
        text1.setText("localhost");
        TextField text2 = new TextField();
        text2.setText("3456");
        TextField text3 = new TextField();
        text3.setText("3457");

        GridPane grid = new GridPane();
        grid.add(label1, 1, 1);
        grid.add(text1, 2, 1);
        grid.add(label2, 1, 2);
        grid.add(text2, 2, 2);
        grid.add(label3, 1, 3);
        grid.add(text3, 2, 3);
        dialog.getDialogPane().setContent(grid);

        ButtonType connectButton = new ButtonType("Połącz", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(connectButton);

        dialog.setResultConverter(b -> {

            if (b == connectButton) {

                return new ConnectionParams(text1.getText(), text2.getText(), text3.getText());
            }

            return null;
        });

        Optional<ConnectionParams> result = dialog.showAndWait();
        waitingDialog = new Alert(INFORMATION, "Proszę czekać. Trwa ładowanie aplikacji...", CANCEL);
        waitingDialog.show();
        showMessageFrame(result.get());
    }

    public void showMessageFrame(ConnectionParams connectionParams) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("view/MessageFrame.fxml"));
        BorderPane layout = loader.load();
        controller = loader.getController();
        controller.setApplication(this);
        controller.setConnectionParams(connectionParams);
        controller.initializeContext();
        primaryStage.setTitle("Encrypted Messanger");
        primaryStage.setScene(new Scene(layout));
        waitingDialog.close();
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public MessageFrameController getController() {
        return controller;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return hostPort;
    }

    public void setPort(String port) {
        this.hostPort = port;
    }
}
