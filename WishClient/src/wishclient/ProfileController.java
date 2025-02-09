package wishclient;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import wishclient.WishClientHandler;
import wishclient.dto.CurrentUser;
import wishclient.dto.Item;
import wishclient.dto.Notification;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import wishclient.dto.User;
import wishclient.dto.Wish;

public class ProfileController implements Initializable {
    private CurrentUser currentUser;
    private Item item;
    private ArrayList <Item> items;
    WishClientHandler clientHandler = WishClient.getClientHandler();
    ArrayList<Notification> notifications = new ArrayList<Notification>();
    private ArrayList<Wish> wishes = new ArrayList<>();

    
    private int currentPage = 0;
    
    public void setClientHandler(WishClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

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
        try {
            // Receive the currentUser request
            String currentUserJson = clientHandler.receiveResponse();

            Gson gson = new Gson();
            currentUser = gson.fromJson(currentUserJson, CurrentUser.class);
            if (currentUser != null) {
                userNametxt.setText(currentUser.getUserName());
                pointstxt.setText(String.valueOf(currentUser.getPoints()));
            }
            
            // wish request
            clientHandler.sendRequest("{\"type\": \"Wish\", \"User\": "+ currentUser.getUserName() +"}\n");
            String serverResponse = clientHandler.receiveResponse();
            Type wishListType = new TypeToken<ArrayList<Wish>>() {}.getType();
            ArrayList<Wish> wishs = gson.fromJson(serverResponse, wishListType);
            wishes = gson.fromJson(serverResponse, wishListType);
            updateWishDisplay();
             
        } catch (IOException e) {
            System.err.println("Error receiving currentUser JSON: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogoutClick(ActionEvent event) throws IOException {
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
        Button clickedButton = (Button) event.getSource();
        TextField[] wishFields = {wishtxt1, wishtxt2, wishtxt3, wishtxt4};

        for (TextField field : wishFields) {
            if (clickedButton.getId().contains(field.getId().replace("wishtxt", "removeBtn"))) {
                Wish wish = (Wish) field.getUserData();

                // Remove Wish from Server
                if (wish != null) {
                    try {
                        clientHandler.sendRequest("{\"type\": \"RemoveWish\", \"WishID\": " + wish.getWishID() + "}\n");
                        String serverResponse = clientHandler.receiveResponse();

                        // Check if the server confirmed deletion
                        if (serverResponse.contains("success")) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Wish Removed Successfully!");                        

                            wishes.removeIf(w -> w.getWishID() == wish.getWishID());

                            updateWishDisplay();
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
    public void handleNotificationButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Notification.fxml"));
        Parent root = loader.load();

        NotificationController notificationController = loader.getController();

        notificationController.setClientHandler(clientHandler);
        notificationController.setCurrentUser(currentUser);

        notificationController.loadNotifications();

        Stage notificationStage = new Stage();
        notificationStage.setTitle("Notifications");
        notificationStage.setScene(new Scene(root));

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        notificationStage.initOwner(currentStage);

        notificationStage.show();
    }
    

    
    @FXML
    public void handleChargePointsButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ChargePoints.fxml"));
        Parent root = loader.load();

        ChargePointsController chargePointsController = loader.getController();

        chargePointsController.setClientHandler(clientHandler);
        chargePointsController.setCurrentUser(currentUser);

        chargePointsController.setProfileController(this); 

        Stage chargePointsStage = new Stage();
        chargePointsStage.setTitle("Charge Points");
        chargePointsStage.setScene(new Scene(root));

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        chargePointsStage.initOwner(currentStage);

        chargePointsStage.show();
    }


    
    @FXML
    private void handleNextButtonAction(ActionEvent event) {
    if ((currentPage + 1) * 4 < wishes.size()) {
        currentPage++;
        updateWishDisplay();
        }
    }
   @FXML
    private void handlePrevButtonAction(ActionEvent event) {
        if (currentPage > 0) {
            currentPage--;
            updateWishDisplay();
        }
    }
    
    public void updatePoints(int newPoints) {
       currentUser.setPoints(newPoints);
       Platform.runLater(() -> {
           pointstxt.setText(String.valueOf(newPoints));
       });
   }

   private void updateWishDisplay() {
       int startIndex = currentPage * 4;

       TextField[] wishFields = {wishtxt1, wishtxt2, wishtxt3, wishtxt4};

       for (int i = 0; i < 4; i++) {
           if (startIndex + i < wishes.size()) {
               Wish wish = wishes.get(startIndex + i);
               wishFields[i].setText(wish.getName()+ " | Price: " + wish.getPrice() + " | Remaining: " + wish.getRemaining());
               wishFields[i].setUserData(wish);
           } else {
               wishFields[i].setText("-"); 
               wishFields[i].setUserData(null); 
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
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }           
    
    @FXML
    private void handleEditButtonAction(ActionEvent event) throws IOException {
        System.out.println("pp");
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
    
    
}