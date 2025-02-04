package wishclint;

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
import static wishclint.WishClint.connectToServer;

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
        clientHandler = WishClint.getClientHandler();
    }

    /**
     * Handles the "Sign Up" button click event.
     */
    @FXML
    private void handleSignUpButtonClick(ActionEvent event) {
        // Switch to the Sign Up scene
        
        WishClint.switchScene("SignUp.fxml", "Sign Up");
    }

    /**
     * Handles the "Log In" button click event.
     */
    @FXML
    private void handleLogInButtonClick(ActionEvent event) {
            if (clientHandler != null && clientHandler.isConnected()) {
                connectToServer("localhost", 6001);
                WishClint.switchScene("Profile.fxml", "Profile");
    }
            else{
            WishClint.showAlert("Connection Error", "Unable to connect to the server at ");
            }

    }
}