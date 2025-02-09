package wishclient;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wishclient.dto.CurrentUser;

public class ChargePointsController implements Initializable {
    @FXML
    private TextField pointsField; // TextField for entering points

    @FXML
    private Button chargeBtn; // Button to handle charge request

    @FXML
    private Button closeBtn; // Button to close the window
    
    private ProfileController profileController; // Add this line

    // Add this method to set the ProfileController reference
    public void setProfileController(ProfileController profileController) {
        this.profileController = profileController;
    }

    private WishClientHandler clientHandler;
    private CurrentUser currentUser;

    public void setClientHandler(WishClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic (if needed)
    }

    /**
     * Handle the "Charge" button action.
     */
   @FXML
private void handleCharge() {
    if (clientHandler == null) {
        System.err.println("Error: ClientHandler is not initialized.");
        return;
    }

    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            try {
                int points = Integer.parseInt(pointsField.getText());
                String request = String.format("{\"type\": \"Points\", \"User\": \"%s\", \"Points\": %d}\n",
                        currentUser.getUserName(), points);
                clientHandler.sendRequest(request);

                String serverResponse = clientHandler.receiveResponse();
                System.out.println("Server Response: " + serverResponse);

                Platform.runLater(() -> {
                    if (serverResponse != null && serverResponse.contains("success")) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Points charged successfully!");
                        
                        // Notify ProfileController to update points
                        if (profileController != null) {
                            profileController.updatePoints(currentUser.getPoints() + points);
                        } else {
                            System.err.println("Error: profileController is null!");
                        }
                    } else {
                        System.out.println("Failed to charge points. Server response: " + serverResponse);
                    }
                });
            } catch (NumberFormatException e) {
                Platform.runLater(() -> {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid input. Please enter a valid number.");
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    showAlert(Alert.AlertType.ERROR, "Error", "Error communicating with the server.");
                });
            }
            return null;
        }
    };

    new Thread(task).start();
}

    /**
     * Handle the "Close" button action.
     */
    @FXML
    private void handleClose() {
        // Get the current stage (Points window)
        Stage stage = (Stage) closeBtn.getScene().getWindow();

        // Close the Points window
        stage.close();
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}