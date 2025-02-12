package wishclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import wishclient.dto.Notification;
import wishclient.dto.CurrentUser;

public class NotificationController implements Initializable {

    private final Gson gson = new Gson();
    private WishClientHandler clientHandler;
    private CurrentUser currentUser;

    @FXML
    private Button closeBtn;
    
    @FXML
    private ListView<String> NotificationListView;  // FIXED: Correctly typed ListView<String>

    public void setClientHandler(WishClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setCurrentUser(CurrentUser currentUser) {
        this.currentUser = currentUser;
    }

    public void loadNotifications() {
        if (clientHandler == null) {
            System.err.println("Error: ClientHandler is not initialized.");
            return;
        }
        if (currentUser == null) {
            System.err.println("Error: CurrentUser is not initialized.");
            return;
        }

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Send a request to fetch notifications
                    clientHandler.sendRequest("{\"type\": \"Notification\", \"User\": \"" + currentUser.getUserName() + "\"}\n");

                    // Receive the server response
                    String serverResponse = clientHandler.receiveResponse();
                    System.out.println("Server Response: " + serverResponse);

                    if (serverResponse == null || serverResponse.isEmpty()) {
                        System.out.println("No notifications received from the server.");
                        return null;
                    }

                    // Parse the server response
                    Type notificationListType = new TypeToken<ArrayList<Notification>>() {}.getType();
                    ArrayList<Notification> notifications = gson.fromJson(serverResponse, notificationListType);

                    // Update the UI on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        NotificationListView.getItems().clear(); // FIXED: Use correct FXML ListView
                        for (Notification notification : notifications) {
                            NotificationListView.getItems().add(notification.getContext());
                        }
                    });

                } catch (IOException e) {
                    Platform.runLater(() -> {
                        NotificationListView.getItems().add("Error loading notifications.");
                    });
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No need to initialize anything here, as setClientHandler() and setCurrentUser() will be called before loading notifications
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) NotificationListView.getScene().getWindow(); // FIXED: Correct ListView reference
        stage.close();
    }
}
