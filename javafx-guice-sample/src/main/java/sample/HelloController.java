package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.inject.Inject;

public class HelloController {

    @FXML
    private Label message;
    @FXML
    private TextField yourName;
    @FXML
    private Button greet;
    @Inject
    private Stage stage;
    @Inject
    private Hello hello;

    public void onGreet(ActionEvent event) {
        message.setText(hello.greet(yourName.getText()));
    }
}
