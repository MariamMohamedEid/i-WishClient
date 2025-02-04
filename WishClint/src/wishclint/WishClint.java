package wishclint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

public class WishClint extends Application {

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
            Parent root = FXMLLoader.load(WishClint.class.getResource(fxml));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title); // Optional: Set the window title
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the scene: " + fxml);
        }
    }

    /**
     * Connects to the server with the specified address and port
     */
public static void connectToServer(String serverAddress, int port) {
    new Thread(() -> {
        try {
            clientHandler = new WishClientHandler(serverAddress, port);
            System.out.println("Successfully connected to the server.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Connection Error", "Unable to connect to the server at " + serverAddress + ":" + port);
        }
    }).start();
}
    /**
     * Disconnects from the server
     */
    public static void disconnectFromServer() {
        if (clientHandler == null) {
            System.err.println("Client handler is not initialized.");
            return;
        }
        try {
            clientHandler.disconnect();
            System.out.println("Disconnected from the server.");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to disconnect from the server.");
        }
    }

    /**
     * Returns the client handler for server communication
     */
    public static WishClientHandler getClientHandler() {
        return clientHandler;
    }



public static void showAlert(String title, String message) {
    Platform.runLater(() -> { // Ensures UI update happens on the JavaFX thread
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    });
}

    public static void main(String[] args) {
        launch(args);
    }
}