package wishclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

public class WishClient extends Application {

    private static Stage primaryStage; // Reference to the primary stage
    private static WishClientHandler clientHandler; // Client handler for server communication

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage; 
        switchScene("LogIn.fxml", "Log In"); 
        stage.show();
    }

    /**
     * Switches scenes function
     */
    public static void switchScene(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(WishClient.class.getResource(fxml));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title); // Optional: Set the window title
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the scene: " + fxml);
        }
    }


private static final Object clientHandlerLock = new Object(); 

public static void connectToServer(String serverAddress, int port) {
    synchronized (clientHandlerLock) {
        try {
            if (clientHandler == null || !clientHandler.isConnected()) {
                clientHandler = new WishClientHandler(serverAddress, port);
                System.out.println("Successfully connected to the server.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Connection Error", "Unable to connect to the server at " + serverAddress + ":" + port);
        }
    }
}

public static void disconnectFromServer() {
    synchronized (clientHandlerLock) {
        if (clientHandler == null) {
            System.err.println("Client handler is not initialized.");
            return;
        }
        try {
            clientHandler.disconnect();
            clientHandler = null; // Set to null after disconnecting
            System.out.println("Disconnected from the server.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to disconnect from the server.");
        }
    }
}

public static WishClientHandler getClientHandler() {
    synchronized (clientHandlerLock) {
        return clientHandler;
    }
}


public static void showAlert(String title, String message) {
    if (Platform.isFxApplicationThread()) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    } else {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}

    public static void main(String[] args) {
        launch(args);
    }
}