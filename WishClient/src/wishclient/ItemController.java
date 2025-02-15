package wishclient;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import wishclient.dto.Item;
import wishclient.WishClientHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import wishclient.dto.CurrentUser;

public class ItemController implements Initializable {
    private CurrentUser currentUser;

    @FXML
    private TableView<Item> producttable;

    @FXML
    private TableColumn<Item, String> productcol;

    @FXML
    private TableColumn<Item, Integer> pricecol;
    
    @FXML
    private TableColumn<Item, Void> addcol;

    @FXML
    private Button back;

    WishClientHandler clientHandler = WishClient.getClientHandler();
    private ObservableList<Item> productList = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            clientHandler = WishClient.getClientHandler();
            if (clientHandler == null) {
                System.err.println("clientHandler is NULL! Initialization failed.");
                return;

            }
            Gson gson = new Gson();
            String currentUserJson = clientHandler.receiveResponse();

            currentUser = gson.fromJson(currentUserJson, CurrentUser.class);

//            System.out.println("clientHandler initialized successfully.");

            productcol.setCellValueFactory(new PropertyValueFactory<>("name"));
            pricecol.setCellValueFactory(new PropertyValueFactory<>("price"));
            
        // Set up "Add to Wishlist" button column
            addcol.setCellFactory(param -> new TableCell<Item, Void>() {
                private final Button addButton = new Button("Add");

                {
                    addButton.setOnAction(event -> {
                        Item item = getTableView().getItems().get(getIndex());
                        handleAddToWishlist(item);
                    });
                }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addButton);
                }
            }
        }); 
            
            
            JsonObject request = new JsonObject();
            request.addProperty("type", "GetItems");

            clientHandler.sendRequest(request.toString());
            String serverResponse = clientHandler.receiveResponse();

            loadProducts(serverResponse);
            producttable.setItems(productList);

        } catch (IOException e) {
            System.err.println("Failed to fetch products: " + e.getMessage());
        }
    }


    private void loadProducts(String jsonResponse) {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Item>>() {}.getType();

            List<Item> items = gson.fromJson(jsonResponse, listType);

            if (items != null) {
                productList.setAll(items);  
//                System.out.println("Products loaded successfully! Total: " + items.size());
            } else {
                System.err.println("Error: Received null or empty item list.");
            }

        } catch (Exception e) {
            System.err.println("Error parsing product list: " + e.getMessage());
        }
    }

    private void handleAddToWishlist(Item item) {
        if (currentUser == null) {
            System.err.println("ERROR: No user logged in.");
            return;
        }

//        System.out.println("Adding to Wishlist: " + item.getName() + " product_id: "+ item.getProduct_ID());

        JsonObject request = new JsonObject();
        request.addProperty("type", "AddWish");
        request.addProperty("User", currentUser.getUserName());
        request.addProperty("ProductID", item.getProduct_ID());

        try {
            // Ensure clientHandler is using the same connection
            if (clientHandler == null) {
                clientHandler = WishClient.getClientHandler();
            }

            clientHandler.sendRequest(request.toString());
            String response = clientHandler.receiveResponse();
            
            if (response.contains("success")) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Wish Add Successfully!");
            }


        } catch (IOException e) {
            System.err.println("ERROR: Failed to add item to wishlist: " + e.getMessage());
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
    private void handleBackButtonAction(ActionEvent event) throws IOException {
        try {
                    clientHandler.sendRequest("{\"type\": \"User\", \"userName\": " + currentUser.getUserName() + "}\n");
                    WishClient.switchScene("Profile.fxml", "Profile");
                } catch (IOException ex) {
                    Logger.getLogger(ProfileController.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
}


