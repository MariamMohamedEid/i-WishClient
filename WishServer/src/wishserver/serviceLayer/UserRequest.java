package wishserver.serviceLayer;


import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.PrintWriter;
import java.net.Socket;
import wishserver.dal.UserAO;
import static wishserver.dal.UserAO.checkUserExist;
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
        
    }

}
