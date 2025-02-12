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
import wishserver.dal.NotificationsAO;
import wishserver.dto.Notification;

public class NotificationRequest {
    
    private final Gson gson;
    
    public NotificationRequest(Gson gson) {
        this.gson = gson;
    }
    
    public void handleNotificationRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try {
            String userName = jsonObject.get("User").getAsString();
            
            ArrayList<Notification> notifications = NotificationsAO.getAllNotifications(userName);
            String jsonResponse = gson.toJson(notifications);
            
            out.println(jsonResponse);
            out.flush();
        } catch (SQLException e) {
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error fetching notifications\"}";
            System.err.println("Error fetching notifications: " + e.getMessage());
            out.println(errorResponse);
            out.flush();
        }
    }
    
    
}
