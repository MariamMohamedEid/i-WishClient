/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wishserver.serviceLayer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import wishserver.dal.WishesAO;
import static wishserver.dal.WishesAO.getAllWishes;
import wishserver.dto.Item;
import wishserver.dto.Wish;

/**
 *
 * @author LENOVO
 */
public class WishRequest {
    private final Gson gson;

    public WishRequest(Gson gson) {
        this.gson = gson;
    }
    
    public void handleGetWishRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try {
            String userName = jsonObject.get("User").getAsString();
            ArrayList<Wish> wishes = getAllWishes(userName);
            String jsonResponse = gson.toJson(wishes);
            System.out.println(jsonResponse);
            out.println(jsonResponse);
            out.flush();

        } catch (SQLException e) {
            // Handle SQL exceptions
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error fetching wishes\"}";
            System.err.println("Error fetching wishes: " + e.getMessage());
            out.println(errorResponse);
            out.flush();
        }
    }
    
    public void handleRemoveRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try{
            int wishID = Integer.parseInt(jsonObject.get("WishID").getAsString());
            String response;
            if(WishesAO.deleteWish(wishID)){
            response = "{\"status\": \"success\", \"message\": \"Wish deleted successfully!\"}";
            out.println(response);
            out.flush();  
            }
            else{
            response = "{\"status\": \"failed\", \"message\": \"Wish failed to be deleted!\"}";
            out.println(response);
            out.flush();
            }
            
        } catch (Exception e) {
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error processing request\"}";
            System.err.println("Error processing request: " + e.getMessage());
            out.println(errorResponse);
            out.flush();
        }        
    }
        
    
    public void handleAddRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try{
            String userName = jsonObject.get("User").getAsString();
            int product_id = Integer.parseInt(jsonObject.get("ProductID").getAsString());
            String response;
            WishesAO.insertWish(userName ,product_id);
            response = "{\"status\": \"success\", \"message\": \"Wish added successfully!\"}";
            out.println(response);
            out.flush();            
        } catch (Exception e) {
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error processing request\"}";
            System.err.println("Error processing request: " + e.getMessage());
            out.println(errorResponse);
            out.flush();
        }
        
    }
    public void handleContributeRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try{
            String userName = jsonObject.get("User").getAsString();
            int wishID = Integer.parseInt(jsonObject.get("WishID").getAsString());
            int amount = Integer.parseInt(jsonObject.get("Amount").getAsString());
            String response;
            WishesAO.wishContribute(userName ,wishID, amount);
            response = "{\"status\": \"success\", \"message\": \"Contribution added successfully!\"}";
            out.println(response);
            out.flush();        
        } catch (Exception e) {
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error processing request\"}";
            System.err.println("Error processing request: " + e.getMessage());
            out.println(errorResponse);
            out.flush();
        }    
    }
}
