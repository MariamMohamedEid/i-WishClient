package wishclient;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import javafx.scene.control.TextField;
import wishclient.dto.CurrentUser;
import wishclient.dto.User;

public class FriendRequestController implements Initializable {

    private CurrentUser currentUser;
    WishClientHandler clientHandler = WishClient.getClientHandler();
    private ArrayList <User> users;
    private int currentPage = 0;
    

    @FXML
    private Button previousBtn;
    @FXML
    private Button nextBtn;


    @FXML
    private TextField userNametxt;
    @FXML
    private Button acceptBtn2;
    @FXML
    private Button removeBtn2;
    @FXML
    private Button acceptBtn3;
    @FXML
    private Button removeBtn3;
    @FXML
    private Button acceptBtn4;
    @FXML
    private Button removeBtn4;
    @FXML
    private Button acceptBtn1;
    @FXML
    private Button removeBtn1;
    @FXML
    private TextField wishtxt1;
    @FXML
    private TextField wishtxt2;
    @FXML
    private TextField wishtxt3;
    @FXML
    private TextField wishtxt4;
    @FXML
    private Button BackBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
                try {
            // Receive the currentUser request
            String currentUserJson = clientHandler.receiveResponse();
            

            Gson gson = new Gson();
            currentUser = gson.fromJson(currentUserJson, CurrentUser.class);
            if (currentUser != null) {
                userNametxt.setText(currentUser.getUserName());
            }
            

            clientHandler.sendRequest("{\"type\": \"GetFriendRequests\", \"User\": "+ currentUser.getUserName() +"}\n");
            String serverResponse = clientHandler.receiveResponse();
//            System.out.println(serverResponse);
            
            Type userListType = new TypeToken<ArrayList<User>>() {}.getType();
            ArrayList<User> wishs = gson.fromJson(serverResponse, userListType);
            users = gson.fromJson(serverResponse, userListType);
            updateUserDisplay();
             
        } catch (IOException e) {
            System.err.println("Error receiving currentUser JSON: " + e.getMessage());
        }
    }


   

    
    @FXML
    private void handleNextButtonAction(ActionEvent event) {
    if ((currentPage + 1) * 4 < users.size()) {
        currentPage++;
        updateUserDisplay();
        }
    }

    @FXML
    private void handlePrevButtonAction(ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            updateUserDisplay();
                }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleRemoveButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        TextField[] wishFields = {wishtxt1, wishtxt2, wishtxt3, wishtxt4};

        for (TextField field : wishFields) {
            if (clickedButton.getId().contains(field.getId().replace("wishtxt", "removeBtn"))) {
                User user = (User) field.getUserData();

                // Remove Wish from Server
                if (user != null) {
                    try {
                        clientHandler.sendRequest("{\"type\": \"RejectFriend\", \"NonFriend\": " + user.getUserName()+ ", \"currentUser\": " + user.getUserName()+ "}\n");
                        String serverResponse = clientHandler.receiveResponse();

                        // Check if the server confirmed deletion
                        if (serverResponse.contains("success")) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Friend Requset removed!");                        

                            users.removeIf(w -> w.getUserName()== user.getUserName());

                            updateUserDisplay();
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Failed to remove wish.");
                        }

                    } catch (IOException ex) {
                        WishClient.showAlert("Remove Wish Error", "An error occurred while removing your wish.");
                    }
                }
                return;
            }
        }
    }

  

    @FXML
    private void handleAcceptButtonAction(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        TextField[] wishFields = {wishtxt1, wishtxt2, wishtxt3, wishtxt4};

        for (TextField field : wishFields) {
            if (clickedButton.getId().contains(field.getId().replace("wishtxt", "acceptBtn"))){
                User user = (User) field.getUserData();

                // Remove Wish from Server
                if (user != null) {
                    try {
                        clientHandler.sendRequest("{\"type\": \"AcceptFriend\", \"NonFriend\": " + user.getUserName()+ ", \"User\": " + currentUser.getUserName()+ "}\n");
                        String serverResponse = clientHandler.receiveResponse();

                        // Check if the server confirmed deletion
                        if (serverResponse.contains("success")) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "You are now Friends!");                        

                            users.removeIf(w -> w.getUserName()== user.getUserName());

                            updateUserDisplay();
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Failed to remove wish.");
                        }

                    } catch (IOException ex) {
                        WishClient.showAlert("Remove Wish Error", "An error occurred while removing your wish.");
                    }
                }
                return;
            }
        }
    }
    
    
    private void shiftWishesUp() {
        TextField[] wishFields = {wishtxt1, wishtxt2, wishtxt3, wishtxt4};

        for (int i = 0; i < wishFields.length - 1; i++) {
            if (wishFields[i].getText().isEmpty() && !wishFields[i + 1].getText().isEmpty()) {
                wishFields[i].setText(wishFields[i + 1].getText());
                wishFields[i].setUserData(wishFields[i + 1].getUserData());

                wishFields[i + 1].clear();
                wishFields[i + 1].setUserData(null);
            }
        }
    }
    
    
       private void updateUserDisplay() {
        int startIndex = currentPage * 4;

        TextField[] wishFields = {wishtxt1, wishtxt2, wishtxt3, wishtxt4};
        for (int i = 0; i < 4; i++) {
            if (startIndex + i < users.size()) {
                User user = users.get(startIndex + i);
                wishFields[i].setText(user.getUserName());
                wishFields[i].setUserData(user);
            } else {
                wishFields[i].setText("-"); 
                wishFields[i].setUserData(null); 
            }
        }
    }
       
    @FXML
    private void handleLogoutClick(ActionEvent event) throws IOException {
        clientHandler.sendRequest("{\"type\": \"User\", \"userName\": "+currentUser.getUserName()+"   }\n");
        WishClient.switchScene("Profile.fxml", "Profile");
    } 
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }  
}
