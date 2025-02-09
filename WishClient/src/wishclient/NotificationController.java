package wishclient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import wishclient.dto.Notification;
import wishclient.dto.CurrentUser;

public class NotificationController implements Initializable {
    @FXML
    private ListView<String> notificationListView;

    private final Gson gson = new Gson();
    private WishClientHandler clientHandler;
    private CurrentUser currentUser;

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

    // Use a background thread to avoid blocking the UI
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
                        notificationListView.getItems().clear(); 
                        for (Notification notification : notifications) {
                            notificationListView.getItems().add(notification.getContext());
                        }

                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    notificationListView.getItems().add("Error loading notifications.");
                });
            }
            System.out.println(currentUser.getUserName());
            return null;
        }
    };

    // Start the task in a new thread
    new Thread(task).start();
}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Do not call loadNotifications here. It should be called after setting clientHandler and currentUser.
    }

    @FXML
    private void handleClose() {
        // Get the current stage (Notification window)
        Stage stage = (Stage) notificationListView.getScene().getWindow();
         
        // Close the Notification window
        stage.close();
    }
}