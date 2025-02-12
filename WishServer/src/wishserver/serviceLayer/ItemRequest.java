/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wishserver.serviceLayer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import static wishserver.dal.ProductsAO.getAllProducts;
import wishserver.dto.Item;

/**
 *
 * @author LENOVO
 */
public class ItemRequest {
    private final Gson gson;

    public ItemRequest(Gson gson) {
        this.gson = gson;
    }
    
    public void handleGetItemRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try {
            ArrayList<Item> items = getAllProducts();
            String jsonResponse = gson.toJson(items);
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
}

