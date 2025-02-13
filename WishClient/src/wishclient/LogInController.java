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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import static wishclient.WishClient.connectToServer;
import static wishclient.WishClient.switchScene;
import wishclient.dto.NewUser;

public class LogInController implements Initializable {

    @FXML
    private Button signUpBtn;

    private WishClientHandler clientHandler;
    @FXML
    private TextField usernametxt;
    @FXML
    private PasswordField passwordtxt;
    @FXML
    private Button loginbtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set up the event handler for the "Sign Up" button
        signUpBtn.setOnAction(this::handleSignUpButtonClick);

        // Set up the event handler for the "Log In" button
        loginbtn.setOnAction(this::handleLogInButtonClick);
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
        try {
            connectToServer("localhost", 7001);
            clientHandler = WishClient.getClientHandler(); 

            if (clientHandler != null && clientHandler.isConnected()) {
                try {
                    // Retrieve input values
                    String username = usernametxt.getText();
                    String password = passwordtxt.getText();

                    // Validate input fields
                    if (username.isEmpty() || password.isEmpty()) {
                        throw new IllegalArgumentException("Both UserName and Password are required to log in");
                    }

                    // Create user data as JSON
                    NewUser newUser = new NewUser(username, password);
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.toJsonTree(newUser).getAsJsonObject();
                    jsonObject.addProperty("type", "LogIn");
                    String userJson = gson.toJson(jsonObject);

                    System.out.println("Sending JSON to server: " + userJson);

                    // Send request and receive response
                    clientHandler.sendRequest(userJson + "\n"); // Ensure request ends with newline
                    System.out.println("Request sent. Waiting for response...");
                    String response = clientHandler.receiveResponse();
                    System.out.println("Response from server: " + response);

                    if (response == null || response.isEmpty()) {
                        showAlert(AlertType.ERROR, "Error", "No response from the server.");
                        return;
                    }

                    // Parse response
                    JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
                    if (jsonResponse.has("status") && jsonResponse.get("status").getAsString().equalsIgnoreCase("success")) {
                        switchScene("Profile.fxml", "Profile");
                        clearInputFields();
                        showAlert(AlertType.INFORMATION, "Success", "Welcome Back ◝(ᵔᵕᵔ)◜!");
                    } else {
                        String errorMsg = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown error";
                        showAlert(AlertType.ERROR, "Error", "Failed to log in: " + errorMsg);
                    }
                } catch (IllegalArgumentException e) {
                    showAlert(AlertType.ERROR, "Input Error", e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(AlertType.ERROR, "Server Error", "Failed to communicate with the server.");
                }
            } else {
                showAlert(AlertType.ERROR, "Connection Error", "Unable to connect to the server.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "An unexpected error occurred.");
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearInputFields() {
        usernametxt.clear();
        passwordtxt.clear();
    }
}
