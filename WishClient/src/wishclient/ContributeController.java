package wishclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wishclient.dto.CurrentUser;
import wishclient.dto.Wish;

public class ContributeController implements Initializable {
    @FXML
    private TextField amounttxt;
    @FXML
    private Button contributebtn;
    @FXML
    private Button closebtn;
    
    private Wish wish;
    
    private WishClientHandler clientHandler;
    private CurrentUser currentUser;
    private FriendWishListController friendWishlistController;

public void setFriendWishlistController(FriendWishListController controller) {
    this.friendWishlistController = controller;
}
    public void setClientHandler(WishClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
    public void setWish(Wish wish) {
    this.wish = wish;
}
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic if needed
    }    
    
    @FXML
    private void handleClose() {
        // Get the current stage (Points window)
        Stage stage = (Stage) closebtn.getScene().getWindow();
        // Close the Points window
        stage.close();
    }
    
@FXML    
private void handleContribute() {
    if (clientHandler == null || wish == null || currentUser == null) {
        System.err.println("Error: ClientHandler, Wish, or CurrentUser is not initialized.");
        return;
    }

    try {
        int points = Integer.parseInt(amounttxt.getText());

        // ✅ Check if points entered are valid
        if (points <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a positive number.");
            return;  // ✅ This is fine outside the Task
        }
        
        // ✅ Prevent over-contribution
        if (points > wish.getRemaining()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Contribution", 
                "You cannot contribute more than the remaining " + wish.getRemaining() + " points.");
            return;
        }

        if (points > currentUser.getPoints()) {
            showAlert(Alert.AlertType.ERROR, "Insufficient Points", 
                "You only have " + currentUser.getPoints() + " points available.");
            return;
        }


        
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    String request = String.format("{\"type\": \"Contribute\", \"User\": \"%s\", \"WishID\": \"%d\", \"Amount\": %d}\n",
                    currentUser.getUserName(), wish.getWishID(), points);
                    clientHandler.sendRequest(request);
                    String serverResponse = clientHandler.receiveResponse();
//                    System.out.println("Server Response: " + serverResponse);

                    Platform.runLater(() -> {
                    if (serverResponse != null && serverResponse.contains("success")) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Points contributed successfully!");

                        if (friendWishlistController != null) {
                            friendWishlistController.updateWishProgress(wish, points);
                            friendWishlistController.updatePoints(currentUser.getPoints() - points);
                        } else {
                            System.err.println("Error: friendWishlistController is null!");
                        }


                        Stage stage = (Stage) contributebtn.getScene().getWindow();
                        stage.close();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to contribute points.");
                    }
                });

                } catch (IOException e) {
                    Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error", "Server error."));
                }
                return null;  // ✅ Ensures Task<Void> has a return value
            }
        };

        new Thread(task).start();

    } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Error", "Invalid input. Enter a valid number.");
    }
}


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    

}
