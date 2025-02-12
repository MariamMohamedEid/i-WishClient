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
        try{
            String userName = jsonObject.get("User").getAsString();
            String nonFriend = jsonObject.get("NonFriend").getAsString();
            String response;
            
            if(FriendRequsetsAO.checkNonFriend(userName,nonFriend)){
                if(FriendRequsetsAO.checkRequest(userName,nonFriend)){
                FriendRequsetsAO.insertFriendReq(userName ,nonFriend);
                response = "{\"status\": \"success\", \"message\": \"Sent successfully!\"}";
                out.println(response);
                out.flush();    
                }
                else{
                    response = "{\"status\": \"success\", \"message\": \"Request already sent!\"}";
                    out.println(response);
                    out.flush(); 
                }
            }
            else{
                response = "{\"status\": \"success\", \"message\": \"you are already Friends!\"}";
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
            System.out.println(jsonResponse);
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
            System.out.println(userName);
            ArrayList<User> friends = FriendRequsetsAO.getAllFriendRequests(userName);
            
            String jsonResponse = gson.toJson(friends);
            System.out.println(jsonResponse);
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
