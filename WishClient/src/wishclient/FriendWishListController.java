package wishclient;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wishclient.dto.CurrentUser;
import wishclient.dto.Wish;

public class FriendWishListController implements Initializable {

    
 
    private CurrentUser currentUser;
    private List<Wish> wishes = new ArrayList<>();
    private int currentPage = 0;
    private final int itemsPerPage = 4;
    private String friendName;
    WishClientHandler clientHandler = WishClient.getClientHandler();
    @FXML
    private Button contributebtn2;
    @FXML
    private Button contributebtn3;
    @FXML
    private Button contributebtn4;
    @FXML
    private Button contributebtn1;

    
    
    public void setClientHandler(WishClientHandler clientHandler) {
    this.clientHandler = clientHandler;
    }
    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }
    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    @FXML
    private Button backbtn;
    @FXML
    private TextField userNametxt;
    @FXML
    private TextArea pointstxt;
    @FXML
    private Button previousbtn;
    @FXML
    private Button nextbtn;
    @FXML
    private TextField wishtxt1;
    @FXML
    private TextField wishtxt2;
    @FXML
    private TextField wishtxt3;
    @FXML
    private TextField wishtxt4;



    @Override
    public void initialize(URL url, ResourceBundle rb) {

           
        try {
            // Receive the currentUser request
            String receivedJson = clientHandler.receiveResponse();
            JsonObject jsonObject = JsonParser.parseString(receivedJson).getAsJsonObject();
            
            String userName = jsonObject.get("myusername").getAsString();
            String friendName = jsonObject.get("friendusername").getAsString();
            int points = jsonObject.get("mypoints").getAsInt();

            Gson gson = new Gson();
            currentUser = new CurrentUser(userName, points);
            if (currentUser != null) {
                userNametxt.setText(currentUser.getUserName());
                pointstxt.setText(String.valueOf(currentUser.getPoints()));
                
            clientHandler.sendRequest("{\"type\": \"Wish\", \"User\": "+ friendName +"}\n");
            String serverResponse = clientHandler.receiveResponse();
            Type wishListType = new TypeToken<ArrayList<Wish>>() {}.getType();
            ArrayList<Wish> wishs = gson.fromJson(serverResponse, wishListType);
            wishes = gson.fromJson(serverResponse, wishListType);
            updateWishDisplay();
            }        
        } catch (IOException e) {
            System.err.println("Error receiving currentUser JSON: " + e.getMessage());
        }
            

           
                 

    }
    
    @FXML
    private void handleBackButton(ActionEvent event) throws IOException{
        clientHandler.sendRequest("{\"type\": \"User\", \"userName\": "+currentUser.getUserName()+"   }\n");
        WishClient.switchScene("Profile.fxml", "Profile");
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
    if (currentUser == null) {
        System.err.println("Error: Current user is null.");
        return;
    }

    currentUser.setPoints(newPoints);
    
    // ✅ Ensure UI updates properly
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
       
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
@FXML
private void handleContributeButtonAction(ActionEvent event) {
    Button clickedButton = (Button) event.getSource();
    TextField[] wishFields = {wishtxt1, wishtxt2, wishtxt3, wishtxt4};
    Button[] contributeButtons = {contributebtn1, contributebtn2, contributebtn3, contributebtn4};

    for (int i = 0; i < contributeButtons.length; i++) {
        if (clickedButton == contributeButtons[i]) {  // Check which button was clicked
            Wish wish = (Wish) wishFields[i].getUserData();
            if (wish != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Contribute.fxml"));
                    Parent root = loader.load();
                        
                    ContributeController contributeController = loader.getController();
                    contributeController.setClientHandler(clientHandler);
                    contributeController.setCurrentUser(currentUser);
                    contributeController.setFriendWishlistController(this);  // Pass reference
                    contributeController.setWish(wish);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Contribute to Wish");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
    }
}

public void updateWishProgress(Wish wish, int points) {
    if (wish == null) {
        System.err.println("Error: Wish is null.");
        return;
    }

    // Find the wish in the list and update it
    for (Wish w : wishes) {
        if (w.getWishID() == wish.getWishID()) {
            int newRemainingAmount = w.getRemaining() - points;
            w.setRemaining(Math.max(newRemainingAmount, 0)); // Prevent negative values
            break;
        }
    }

    // Refresh UI
    Platform.runLater(() -> updateWishDisplay());
}

    
    
}
