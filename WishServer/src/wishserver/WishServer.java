package wishserver;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WishServer extends Application {

    private WishServerHandler server;
    private static final Logger logger = Logger.getLogger(WishServer.class.getName());
    private Thread serverThread;
    private static final int port = 7001;

    @Override
    public void start(Stage stage) {
        try {
            // Load the FXML for the UI
            Parent root = FXMLLoader.load(getClass().getResource("Server.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setOnCloseRequest(event -> {
                try {
                    stop();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error while stopping server", e);
                }
            });
            stage.show();

            // Start the server in a separate thread
            server = new WishServerHandler();
            serverThread = new Thread(() -> {
                try {
                    server.startServer(port);                                     
                    logger.info("Server started successfully.");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Server failed to start", e);
                }
            });

            serverThread.setDaemon(true); // Ensures the thread stops when JavaFX exits
            serverThread.start();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load FXML", e);
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            try {
                server.stopServer();
            } catch (InterruptedException ex) {
                Logger.getLogger(WishServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            logger.info("Server stopped.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
