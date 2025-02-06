package wishclient;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import wishclient.WishClientHandler;
import wishclient.dto.CurrentUser;
import wishclient.dto.Item;

public class ProfileController implements Initializable {
    private CurrentUser currentUser;
    private Item item;
    private ArrayList <Item> items;

    @FXML
    private Button logOutBtn;
    @FXML
    private Button notificationbtn;
    @FXML
    private Button myfriendbtn;
    @FXML
    private Button addfriendbtn;
    @FXML
    private Button friendreqbtn;
    @FXML
    private Button chargepointsbtn;
    @FXML
    private TextField userNametxt;
    @FXML
    private TextArea pointstxt;
    @FXML
    private Button previousBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Button removeBtn2;
    @FXML
    private Button removeBtn3;
    @FXML
    private Button removeBtn4;
    @FXML
    private Button removeBtn1;
    @FXML
    private Button editBtn;
    @FXML
    private TextField wishtxt2;
    @FXML
    private TextField wishtxt1;
    @FXML
    private TextField wishtxt3;
    @FXML
    private TextField wishtxt4;



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Receive the currentUser JSON from the server
        WishClientHandler clientHandler = WishClient.getClientHandler();
        if (clientHandler == null) {
            System.out.println("Error: clientHandler is null!");
            return;
        }

        try {
            // Receive the currentUser JSON
            String currentUserJson = clientHandler.receiveResponse();
            System.out.println("Received currentUser JSON: " + currentUserJson);

            // Parse the JSON into a User object
            Gson gson = new Gson();
            currentUser = gson.fromJson(currentUserJson, CurrentUser.class);

            // Update the UI with the currentUser data
            if (currentUser != null) {
                userNametxt.setText(currentUser.getUserName());
                pointstxt.setText(String.valueOf(currentUser.getPoints()));
            }
        } catch (IOException e) {
            System.err.println("Error receiving currentUser JSON: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogoutClick(ActionEvent event) throws IOException {
        WishClientHandler clientHandler = WishClient.getClientHandler();
        if (clientHandler != null && clientHandler.isConnected()) {
            clientHandler.sendRequest("{\"type\": \"LogOut\"}\n");
            String serverResponse = clientHandler.receiveResponse();
            if (serverResponse != null && serverResponse.contains("Logged out successfully")) {
                WishClient.disconnectFromServer();
                WishClient.switchScene("LogIn.fxml", "Log In");
            } else {
                WishClient.showAlert("Logout Error", "An error occurred while logging out. Please try again.");
            }
        } else {
            WishClient.showAlert("Connection Error", "Not connected to the server.");
        }
    }
    
    @FXML
    private void handleRemoveButtonAction(ActionEvent event) {
        System.out.println("Remove button clicked!");
    }
    @FXML
    private void handleEditButtonAction(ActionEvent event) {
        System.out.println("Edit button clicked!");
    }
    @FXML
    private void handleNotificationButtonAction(ActionEvent event) {
        System.out.println("Edit button clicked!");
    }
    @FXML
    private void handleMyFriendsButtonAction(ActionEvent event) {
        System.out.println("Edit button clicked!");
    }
    @FXML
    private void handleAddFriendButtonAction(ActionEvent event) {
        System.out.println("Edit button clicked!");
    }
    @FXML
    private void handleFriendRequestButtonAction(ActionEvent event) {
        System.out.println("Edit button clicked!");
    }
    @FXML
    private void handleChargePointsButtonAction(ActionEvent event) {
        System.out.println("Edit button clicked!");
    }
    @FXML
    private void handleNextButtonAction(ActionEvent event) {
        System.out.println("Edit button clicked!");
    }
    @FXML
    private void handlePrevButtonAction(ActionEvent event) {
        System.out.println("Edit button clicked!");
    }
}