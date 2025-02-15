package wishclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.IOException;

public class WishClient extends Application {

    private static Stage primaryStage; // Reference to the primary stage
    private static WishClientHandler clientHandler; // Client handler for server communication
    private static final Object clientHandlerLock = new Object(); // Lock for thread safety
    private static final String ip = "localhost";
    private static final int port = 7001;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        switchScene("LogIn.fxml", "Log In"); 
        stage.show();
        
        // Handle cleanup on close
        primaryStage.setOnCloseRequest(event -> {
            System.out.println("Application is closing...");
            disconnectFromServer();
        });
    }

    /**
     * Switches scenes function
     */
    public static void switchScene(String fxml, String title) {
        try {
            Parent root = FXMLLoader.load(WishClient.class.getResource(fxml));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the scene: " + fxml);
        }
    }

    /**
     * Connect to server function
     */
    public static void connectToServer() {
        synchronized (clientHandlerLock) {
            try {
                if (clientHandler == null || !clientHandler.isConnected()) {
                    
                    clientHandler = new WishClientHandler(ip, port);
                    System.out.println("Successfully connected to the server.");
                }
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Connection Error", "Unable to connect to the server at " + ip + ":" + port);
                clientHandler = null; // Reset clientHandler if connection fails
            }
        }
    }

    /**
     * Disconnect from server function
     */
    public static void disconnectFromServer() {
        synchronized (clientHandlerLock) {
            if (clientHandler != null) {
                try {
                    clientHandler.disconnect();
                    System.out.println("Disconnected from the server.");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Failed to disconnect from the server.");
                } finally {
                    clientHandler = null; // Ensure clientHandler is set to null
                }
            }
        }
    }

    /**
     * Returns the client handler instance
     */
    public static WishClientHandler getClientHandler() {
        synchronized (clientHandlerLock) {
            return clientHandler;
        }
    }

    /**
     * Show alert function
     */
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

    /**
     * Application main function
     */
    public static void main(String[] args) {
        launch(args);
    }
}
