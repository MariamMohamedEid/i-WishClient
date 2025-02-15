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
import wishserver.dal.FriendRequsetsAO;
import wishserver.dal.FriendsAO;
import wishserver.dal.NotificationsAO;
import static wishserver.dal.UserAO.checkUserExist;
import static wishserver.dal.UserAO.checkUserExist2;
import static wishserver.dal.UserAO.getCurrentUser;
import wishserver.dto.CurrentUser;
import wishserver.dto.User;

/**
 *
 * @author LENOVO
 */
public class FriendRequest {
    private final Gson gson;

    public FriendRequest(Gson gson) {
        this.gson = gson;
    }
    
    
    
public void handleAddRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
    try {
        String userName = jsonObject.get("User").getAsString();
        String nonFriend = jsonObject.get("NonFriend").getAsString();
        String response;


        if (!checkUserExist2(nonFriend)) { // Ensure the receiver exists
            response = "{\"status\": \"error\", \"message\": \"User not found!\"}";
            out.println(response);
            out.flush();
            return;
        }

        // Check if they are already friends
        if (FriendRequsetsAO.checkNonFriend(userName, nonFriend)) {
            // Check if a friend request is already sent
            if (FriendRequsetsAO.checkRequest(userName, nonFriend)) {
                FriendRequsetsAO.insertFriendReq(userName, nonFriend);
                response = "{\"status\": \"success\", \"message\": \"Request sent successfully!\"}";
            } else {
                response = "{\"status\": \"success\", \"message\": \"Request already sent!\"}";
            }
        } else {
            response = "{\"status\": \"error\", \"message\": \"You are already friends!\"}";
        }

        out.println(response);
        out.flush();

    } catch (Exception e) {
        String errorResponse = "{\"status\": \"error\", \"message\": \"Internal Server Error\"}";
        System.err.println("Error processing request: " + e.getMessage());
        e.printStackTrace();
        out.println(errorResponse);
        out.flush();
    }
}

    public void handleRemoveRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try{
            String userName = jsonObject.get("User").getAsString();
            String Friend = jsonObject.get("Friend").getAsString();
            String response;            
            FriendsAO.removeFriend(userName ,Friend);
            response = "{\"status\": \"success\", \"message\": \"Friend Removed successfully!\"}";
            out.println(response);
            out.flush();    
        } catch (Exception e) {
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error processing request\"}";
            System.err.println("Error processing request: " + e.getMessage());
            out.println(errorResponse);
            out.flush();
        }
    }   
    public void handleAcceptRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try{
            String userName = jsonObject.get("User").getAsString();
            String Friend = jsonObject.get("NonFriend").getAsString();
            String response;            
            FriendsAO.insertFriend(userName ,Friend);
            FriendsAO.insertFriend(Friend ,userName);
            FriendRequsetsAO.deleteFriendReq(userName ,Friend);
            String notificationContext = "You and "+userName+" are now friends!";
            NotificationsAO.createNotification(notificationContext, Friend);
            response = "{\"status\": \"success\", \"message\": \"You are now Friends!\"}";
            out.println(response);
            out.flush();    
        } catch (Exception e) {
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error processing request\"}";
            System.err.println("Error processing request: " + e.getMessage());
            out.println(errorResponse);
            out.flush();
        }
        
    }
    public void handleRejectRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) { 
        try{
            String userName = jsonObject.get("User").getAsString();
            String Friend = jsonObject.get("NonFriend").getAsString();
            String response;            
            FriendRequsetsAO.deleteFriendReq(userName ,Friend);
            response = "{\"status\": \"success\", \"message\": \"Rejected Request!\"}";
            out.println(response);
            out.flush();    
        } catch (Exception e) {
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error processing request\"}";
            System.err.println("Error processing request: " + e.getMessage());
            out.println(errorResponse);
            out.flush();
        }
        
    }
    public void handleGetFriendsRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try {
            String userName = jsonObject.get("User").getAsString();
            ArrayList<User> friends = FriendsAO.getAllFriends(userName);

            String jsonResponse = gson.toJson(friends);
//            System.out.println(jsonResponse);
            out.println(jsonResponse);
            out.flush();

        } catch (SQLException e) {
            // Handle SQL exceptions
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error fetching friends\"}";
            System.err.println("Error fetching friends: " + e.getMessage());
            out.println(errorResponse);
            out.flush();
        }
    }
    public void handleGetFriendRequestsRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try {
            String userName = jsonObject.get("User").getAsString();
//            System.out.println(userName);
            ArrayList<User> friends = FriendRequsetsAO.getAllFriendRequests(userName);
            
            String jsonResponse = gson.toJson(friends);
//            System.out.println(jsonResponse);
            out.println(jsonResponse);
            out.flush();

        } catch (SQLException e) {
            // Handle SQL exceptions
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error fetching friends\"}";
            System.err.println("Error fetching friends: " + e.getMessage());
            out.println(errorResponse);
            out.flush();
        }
    }    

    
    
}
