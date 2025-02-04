package wishclint;

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
import static wishclint.WishClint.connectToServer;
import static wishclint.WishClint.switchScene;
import wishclint.dto.NewUser;

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
    @FXML
    private Label loginLabel;

    @FXML
    private ToggleGroup genderGroup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        maleBtn.setToggleGroup(genderGroup);
        femaleBtn.setToggleGroup(genderGroup);
        maleBtn.setSelected(true);
        clientHandler = WishClint.getClientHandler();

        if (loginLabel != null) {
            loginLabel.setOnMouseClicked(this::handleLoginLabelClick);
        }
    }

    @FXML
private void handleCreateButtonAction(ActionEvent event) {
    
    if (clientHandler != null && clientHandler.isConnected()) {
        connectToServer("localhost", 6001);
        try {
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            String fullName = fullnameInput.getText();
            String ageText = ageInput.getText();
            String phone = phoneInput.getText();

            // Determine the selected gender
            String gender = (maleBtn.isSelected()) ? "Male" : "Female";

        // Validate input fields
        if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || phone.isEmpty() || ageText.isEmpty()) {
            throw new IllegalArgumentException("All fields are required.");
        }

        // Validate age
        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Age must be a valid number.");
        }

        // Create user data as JSON
        NewUser newUser = new NewUser(username, password, fullName, age, gender, phone);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.toJsonTree(newUser).getAsJsonObject();
        jsonObject.addProperty("type", "SignUp");
        String userJson = gson.toJson(jsonObject);

        System.out.println("Sending JSON to server: " + userJson);

        // Ensure client is connected
        WishClientHandler clientHandler = WishClint.getClientHandler();
        if (clientHandler == null || !clientHandler.isConnected()) {
            showAlert(AlertType.ERROR, "Error", "Not connected to the server.");
            return;
        }

        // Send request and receive response
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
    }
    else{
        WishClint.showAlert("Connection Error", "Unable to connect to the server at ");
    }
    
}


    private void clearInputFields() {
        usernameInput.clear();
        passwordInput.clear();
        fullnameInput.clear();
        ageInput.clear();
        phoneInput.clear();
        maleBtn.setSelected(true);
    }

    @FXML
    private void handleLoginLabelClick(MouseEvent event) {
        // Switch to the Log In scene
        WishClint.switchScene("LogIn.fxml", "Log In");
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
