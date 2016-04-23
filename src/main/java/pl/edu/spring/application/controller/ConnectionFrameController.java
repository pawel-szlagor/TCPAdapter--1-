package pl.edu.spring.application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import pl.edu.spring.application.StartMessageApplication;
import pl.edu.spring.tcp.support.CustomContextLoader;

@Controller
@ContextConfiguration(loader = CustomContextLoader.class, locations = "/META-INF/spring-config.xml")
@DirtiesContext
public class ConnectionFrameController {
    @FXML
    private Button connectButton;
    @FXML
    private TextField ipHostTextField;
    @FXML
    private TextField portTextField;
    private StartMessageApplication application;


    public ConnectionFrameController() {
    }


    public void setApplication(StartMessageApplication application) {
        this.application = application;
    }



    //JAVA FX ACTIONS

    public void connect() throws Throwable {
        application.showMessageFrame(null);
        super.finalize();
    }

}
