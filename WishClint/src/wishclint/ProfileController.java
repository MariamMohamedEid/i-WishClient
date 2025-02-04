/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wishclint;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class ProfileController implements Initializable {
    
    @FXML
    private TextField userName_txt;
    @FXML
    private TextField userName_txt1;
    @FXML
    private Button logOutBtn;
    

    @FXML
    private void handleBackClick(ActionEvent event) throws IOException { 
        WishClientHandler clientHandler = WishClint.getClientHandler();
        if (clientHandler == null) {
            System.out.println("Error: clientHandler is null!");
            return; // Prevent further execution
        }
        clientHandler.sendRequest("{\"type\": \"LogOut\"}" + "\n");
        WishClint.switchScene("LogIn.fxml","Log In");        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
