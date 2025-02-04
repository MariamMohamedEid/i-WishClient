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

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Load the FXML for the UI
            Parent root = FXMLLoader.load(getClass().getResource("Server.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            server = new WishServerHandler();
            new Thread(() -> {
                try {
                    server.startServer(6001); 
                    logger.info("Server started successfully.");
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Server failed to start", e);
                }
            }).start();

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load FXML", e);
            throw e;
        }
    }

    @Override
    public void stop() throws Exception {
        if (server != null) {
            server.stopServer();  
            logger.info("Server stopped.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
