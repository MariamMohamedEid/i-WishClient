package wishclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import wishclient.dto.User;
import wishclient.dto.CurrentUser;
import wishclient.WishClientHandler;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import wishclient.WishClient;

public class FriendController implements Initializable {

    private CurrentUser currentUser;
    private ObservableList<String> friendsList = FXCollections.observableArrayList();
    WishClientHandler clientHandler = WishClient.getClientHandler();

    @FXML
    private Button delfriend;
    @FXML
    private ListView<String> friendListView;
    @FXML
    private Button viewishlist;
    @FXML
    private Button backbtn;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Receive the currentUser request
            String currentUserJson = clientHandler.receiveResponse();

            Gson gson = new Gson();
            currentUser = gson.fromJson(currentUserJson, CurrentUser.class);
            JsonObject request = new JsonObject();
            request.addProperty("type", "GetFriends");
            request.addProperty("User", currentUser.getUserName());
            clientHandler.sendRequest(request.toString());
            String serverResponse = clientHandler.receiveResponse();
            loadFriends(serverResponse);

            

        } catch (IOException e) {
            System.err.println("Error receiving currentUser JSON: " + e.getMessage());
        }

        friendListView.setItems(friendsList);
        delfriend.setOnAction(event -> handleDeleteFriend());
        viewishlist.setOnAction(event -> handleViewWishlist());
        backbtn.setOnAction(event -> handleBack());

    }

    public void setClientHandler(WishClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
    public void loadFriends(String jsonResponse) {
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            System.out.println("Error: JSON response is null or empty.");
            return;
    }

    Gson gson = new Gson();
    try {
        User[] friendsArray = gson.fromJson(jsonResponse, User[].class);

        if (friendsArray == null) {
            System.out.println("Error: Parsed JSON returned null.");
            return;
        }

        List<User> friends = Arrays.asList(friendsArray);
        System.out.println("Friends loaded successfully.");
        updateFriendsList(friends);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    private void updateFriendsList(List<User> friends) {
        // Clear the existing list
        friendsList.clear();

        // Add each friend to the ObservableList
        for (User friend : friends) {
            friendsList.add(friend.getUserName());
        }
    }
    

    private void handleDeleteFriend() {
        
        System.out.println("DEBUG: Delete friend clicked");
        
        // Debugging: Check if UI elements are initialized  
        if (friendListView == null) {
        System.err.println("Error: friendListView is null!");
        return;
    }
        // Get the selected friend from the ListView
        String selectedFriend = friendListView.getSelectionModel().getSelectedItem();

        if (selectedFriend == null) {
            System.out.println("No friend selected.");
            return;
        }
        System.out.println("DEBUG: Selected friend = " + selectedFriend);        

                // Debugging: Check if currentUser is initialized
        if (currentUser == null) {
            System.err.println("Error: currentUser is null!");
            return;
        }
        System.out.println("DEBUG: Current user = " + currentUser.getUserName());
        
    // Debugging: Check if clientHandler is initialized
        if (clientHandler == null) {
            System.err.println("Error: clientHandler is null!");
            return;
        }
        
        
        JsonObject request = new JsonObject();
        request.addProperty("type", "RemoveFriend");
        request.addProperty("User", currentUser.getUserName());  
        request.addProperty("Friend", selectedFriend);  

        try {
            clientHandler.sendRequest(request.toString());

            String serverResponse = clientHandler.receiveResponse();
        System.out.println("DEBUG: Server response = " + serverResponse);
            Gson gson = new Gson();
            JsonObject jsonResponse = gson.fromJson(serverResponse, JsonObject.class);
            
            if (jsonResponse.has("status") && jsonResponse.get("status").getAsString().equals("success")) {
                friendsList.remove(selectedFriend);
                System.out.println("Friend removed successfully: " + selectedFriend);
            } else {
                System.out.println("Error: " + jsonResponse.get("message").getAsString());
            }

        } catch (IOException e) {
            System.err.println("Error sending request to remove friend: " + e.getMessage());
        }
    }


    private void handleViewWishlist() {

        String selectedFriend = friendListView.getSelectionModel().getSelectedItem();
        if (selectedFriend == null) {
            System.out.println("No friend selected.");

        }else{
            try {
                clientHandler.sendRequest("{\"type\": \"getFriendWishes\", \"FriendUser\": "+selectedFriend+", \"CurrentUser\": "+currentUser.getUserName()+"   }\n");
                WishClient.switchScene("FriendWishList.fxml", "FriendWishList");
            } catch (IOException ex) {
                Logger.getLogger(FriendController.class.getName()).log(Level.SEVERE, null, ex);
            }

            
        }
    }

    private void handleBack() {
        try {
            clientHandler.sendRequest("{\"type\": \"User\", \"userName\": "+currentUser.getUserName()+"   }\n");
        } catch (IOException ex) {
            Logger.getLogger(FriendController.class.getName()).log(Level.SEVERE, null, ex);
        }
        WishClient.switchScene("Profile.fxml", "Profile");
    }



}

