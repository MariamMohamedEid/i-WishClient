/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wishclient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class AddFriendController implements Initializable {

    @FXML
    private Button closeBtn;
    @FXML
    private Button SendBtn;
    @FXML
    private TextField txtField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    private WishClientHandler clientHandler;
    private CurrentUser currentUser;

    public void setClientHandler(WishClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    @FXML
    private void handleClose() {
        // Get the current stage (Points window)
        Stage stage = (Stage) closeBtn.getScene().getWindow();

        // Close the Points window
        stage.close();
    }


    
     @FXML
private void handleSend() {
    if (clientHandler == null) {
//        System.err.println("Error: ClientHandler is not initialized.");
        return;
    }

    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            String nonFriend = txtField.getText();
            if (nonFriend.equals(currentUser.getUserName())) {
                    showAlert(Alert.AlertType.ERROR, "Error", "You cannot add yourself as a friend.");
                    return null;
                }else{
            String request = String.format("{\"type\": \"AddFriend\", \"User\": \"%s\", \"NonFriend\": %s}\n",
            currentUser.getUserName(), nonFriend);
            clientHandler.sendRequest(request);
            String serverResponse = clientHandler.receiveResponse();
            JsonObject jsonObject = JsonParser.parseString(serverResponse).getAsJsonObject();
            Platform.runLater(() -> {
                if (serverResponse != null && serverResponse.contains("success")) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", jsonObject.get("message").getAsString());
                    
                }else if(serverResponse != null && serverResponse.contains("error")) {
                    showAlert(Alert.AlertType.ERROR, "Error", jsonObject.get("message").getAsString());
                }
 
                else {
//                    System.out.println("Failed to send Request. Server response: " + serverResponse);
                }
            });
            return null;
        }
        }
    };

    new Thread(task).start();
}

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}
