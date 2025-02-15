package wishclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import javafx.scene.control.PasswordField;
import static wishclient.WishClient.connectToServer;
import static wishclient.WishClient.disconnectFromServer;
import static wishclient.WishClient.switchScene;
import wishclient.dto.NewUser;

public class SignUpController implements Initializable {
    
    private WishClientHandler clientHandler;

    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private TextField fullnameInput;
    @FXML
    private TextField ageInput;
    @FXML
    private TextField phoneInput;
    @FXML
    private RadioButton maleBtn;
    @FXML
    private RadioButton femaleBtn;
    @FXML
    private Button createBtn;
    private Label loginLabel;

    @FXML
    private ToggleGroup genderGroup;
    @FXML
    private PasswordField confirmPasswordInput;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        maleBtn.setToggleGroup(genderGroup);
        femaleBtn.setToggleGroup(genderGroup);
        maleBtn.setSelected(true);
        clientHandler = WishClient.getClientHandler();

        if (loginLabel != null) {
            loginLabel.setOnMouseClicked(this::handleLoginLabelClick);
        }
    }

@FXML
private void handleCreateButtonAction(ActionEvent event) {
    try {
        // Connect to the server
        connectToServer();                                              
        clientHandler = WishClient.getClientHandler();

        // Check if the client is connected
        if (clientHandler != null && clientHandler.isConnected()) {
            try {
                // Retrieve input values
                String username = usernameInput.getText();
                String password = passwordInput.getText();
                String password2 = confirmPasswordInput.getText();
                String fullName = fullnameInput.getText();
                String ageText = ageInput.getText();
                String phone = phoneInput.getText();

                // Determine the selected gender
                String gender = (maleBtn.isSelected()) ? "Male" : "Female";
                
                // regix for userName
                String usernamePattern = "^[a-zA-Z0-9_]+$";

                // Validate input fields
                if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || phone.isEmpty() || ageText.isEmpty() || password2.isEmpty()) {
                    throw new IllegalArgumentException("All fields are required.");
                }
                if (!password.equals(password2)) {
                    throw new IllegalArgumentException("Password and Confirm Password do not match. Please try again.");
                }
                if (!username.matches(usernamePattern)) {
                    throw new IllegalArgumentException("Username can only contain letters, numbers, and underscores (_).");
                }

                // Validate age
                int age = 0;
                try {
                    age = Integer.parseInt(ageText);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Age must be a valid number.");
                }

                NewUser newUser = new NewUser(username, password, fullName, age, gender, phone);
                Gson gson = new Gson();
                JsonObject jsonObject = gson.toJsonTree(newUser).getAsJsonObject();
                jsonObject.addProperty("type", "SignUp");
                String userJson = gson.toJson(jsonObject);

//                System.out.println("Sending JSON to server: " + userJson);

                clientHandler.sendRequest(userJson + "\n"); 
//                System.out.println("Request sent. Waiting for response...");
                String response = clientHandler.receiveResponse();
//                System.out.println("Response from server: " + response);

                if (response == null || response.isEmpty()) {
                    showAlert(AlertType.ERROR, "Error", "No response from the server.");
                    return;
                }

                JsonObject jsonResponse = gson.fromJson(response, JsonObject.class);
                if (jsonResponse.has("status") && jsonResponse.get("status").getAsString().equalsIgnoreCase("success")) {
                    switchScene("Profile.fxml", "Profile");

                    showAlert(AlertType.INFORMATION, "Success", "User created successfully!");
                } else {
                    String errorMsg = jsonResponse.has("message") ? jsonResponse.get("message").getAsString() : "Unknown error";
                    showAlert(AlertType.ERROR, "Error", "Failed to create user: " + errorMsg);
                }
            } catch (IllegalArgumentException e) {
                showAlert(AlertType.ERROR, "Input Error", e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Server Error", "Failed to communicate with the server.");
            }
        } else {
            WishClient.showAlert("Connection Error", "Unable to connect to the server.");
        }
    } catch (Exception e) {
        e.printStackTrace();
        showAlert(AlertType.ERROR, "Error", "An unexpected error occurred.");
    }
}

    @FXML
    private void handleLoginLabelClick(MouseEvent event) {
        // Switch to the Log In scene
        WishClient.switchScene("LogIn.fxml", "Log In");
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
