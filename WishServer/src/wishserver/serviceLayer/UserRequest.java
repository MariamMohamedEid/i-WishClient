package wishserver.serviceLayer;


import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import wishserver.dal.UserAO;
import static wishserver.dal.UserAO.checkUserExist;
import static wishserver.dal.UserAO.getCurrentUser;
import static wishserver.dal.UserAO.updateUserPoints;
import static wishserver.dal.UserAO.validateUser;
import wishserver.dto.CurrentUser;
import wishserver.dto.NewUser;

public class UserRequest {

    private final Gson gson;

    public UserRequest(Gson gson) {
        this.gson = gson;
    }

    public void handleSignUpRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try {
            NewUser newUser = gson.fromJson(jsonObject, NewUser.class);
            System.out.println("New User Created: " + newUser.getFullName());

            String response;
            System.out.println("Sending response:");
            if(!checkUserExist(newUser)){
                UserAO.insertUser(newUser);
                JsonElement jsonElement = gson.toJsonTree(newUser);
                
                response = "{\"status\": \"success\", \"message\": \"User created successfully!\"}";
                out.println(response);
                out.flush(); 
                
                CurrentUser currentUser = UserAO.getUser(newUser);
                String currentUserJson = gson.toJson(currentUser);
                
                out.println(currentUserJson);
                out.flush();        
            }else{
                response  = "{\"status\": \"Faild\", \"message\": \"User already exist!\"}";
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
    
    public void handleLogInRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try {
            NewUser newUser = gson.fromJson(jsonObject, NewUser.class);
            String response;
            if(validateUser(newUser)){
                JsonElement jsonElement = gson.toJsonTree(newUser);
                
                response = "{\"status\": \"success\", \"message\": \"Welcome Back ◝(ᵔᵕᵔ)◜!\"}";
                out.println(response);
                out.flush(); 
                
                CurrentUser currentUser = UserAO.getUser(newUser);
                String currentUserJson = gson.toJson(currentUser);
                
                out.println(currentUserJson);
                out.flush();        
            }else{
                response  = "{\"status\": \"Failed\", \"message\": \"Wrong username or password !\"}";
                out.println(response);
                out.flush();   
            }   
            
        } catch (Exception e) {
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error processing request\"}";
            out.println(errorResponse);
            out.flush();
        }
    }
    
    public void handleChargeRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try {
            // Extract username and points from the JSON request
            String userName = jsonObject.get("User").getAsString();
            int pointsToAdd = jsonObject.get("Points").getAsInt();
            // Attempt to update user points
            boolean pointsUpdated = updateUserPoints(userName, pointsToAdd);
            // Send response about points charging
            if (pointsUpdated) {
                String response = "{\"status\": \"success\", \"message\": \"Points charged successfully!\"}";
                out.println(response);
            } else {

                String response = "{\"status\": \"failed\", \"message\": \"Failed to charge points.\"}";
                out.println(response);
            }
            out.flush();

        } catch (Exception e) {
            // Error handling
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error processing charge request\"}";
            out.println(errorResponse);
            out.flush();
        }
    }
    public void handleGetRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
        try {
            String userName = jsonObject.get("userName").getAsString();
            CurrentUser currentUser = UserAO.getCurrentUser(userName);
            String currentUserJson = gson.toJson(currentUser); 
            out.println(currentUserJson);
            out.flush();
        } catch (Exception e) {
            // Error handling
            String errorResponse = "{\"status\": \"error\", \"message\": \"Error processing charge request\"}";
            out.println(errorResponse);
            out.flush();
        }
    }
    
    public void handleGetFriendWishRequest(JsonObject jsonObject, PrintWriter out, Socket clientSocket) {
    try {
        String friendUserName = jsonObject.get("FriendUser").getAsString(); // Get friend's username
        String userName = jsonObject.get("CurrentUser").getAsString();
        System.out.println(userName);
        System.out.println(friendUserName);
        CurrentUser currentUser = getCurrentUser(userName);

        String response = "{\"myusername\": "+userName+", \"mypoints\": "+String.valueOf(currentUser.getPoints())+", \"friendusername\": "+friendUserName+"}";
        out.println(response);
        out.flush();
    } catch (SQLException e) {
        String errorResponse = "{\"status\": \"error\", \"message\": \"Error fetching friend's wishes\"}";
        out.println(errorResponse);
        out.flush();
        }
        }
    }


