package wishclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import static wishclient.WishClient.connectToServer;

public class LogInController implements Initializable {

    @FXML
    private Button signUpBtn;
    @FXML
    private Button logInBtn;

    private WishClientHandler clientHandler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up the event handler for the "Sign Up" button
        signUpBtn.setOnAction(this::handleSignUpButtonClick);

        // Set up the event handler for the "Log In" button
        logInBtn.setOnAction(this::handleLogInButtonClick);

        // Initialize the client handler
    }

    /**
     * Handles the "Sign Up" button click event.
     */
    @FXML
    private void handleSignUpButtonClick(ActionEvent event) {
        // Switch to the Sign Up scene
        
        WishClient.switchScene("SignUp.fxml", "Sign Up");
    }

    @FXML
    private void handleLogInButtonClick(ActionEvent event) {
        connectToServer("localhost", 7001);
        clientHandler = WishClient.getClientHandler(); 
        if (clientHandler != null && clientHandler.isConnected()) {
            WishClient.switchScene("Profile.fxml", "Profile");
        } else {
            WishClient.showAlert("Connection Error", "Unable to connect to the server. Please try again.");
        }
    }
}