package wishserver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.net.SocketException;
import wishserver.dto.NewUser;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import wishserver.dal.FriendRequsetsAO;
import wishserver.serviceLayer.ItemRequest;
import wishserver.serviceLayer.NotificationRequest;
import wishserver.serviceLayer.UserRequest;
import wishserver.serviceLayer.WishRequest;
import wishserver.serviceLayer.FriendRequest;

public class WishServerHandler {

    private ServerSocket serverSocket;
    private boolean isRunning = true;
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private Gson gson = new Gson();


    public void startServer(int port) throws InterruptedException {
        try {
            serverSocket = new ServerSocket(port);
//            System.out.println("Server started on port " + port);

            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.execute(new ClientHandler(clientSocket, gson));
                } catch (IOException e) {
                    if (isRunning) {
                        System.err.println("Error accepting client connection: " + e.getMessage());
                    } else {
                        System.out.println("Server is shutting down.");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stopServer();
        }
    }


    public void stopServer() throws InterruptedException {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadPool.shutdown(); 
        if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
            threadPool.shutdownNow(); 
        }
    }

    public class ClientHandler implements Runnable {
        private final Gson gson;
        private final Socket clientSocket;
        private final UserRequest UserRequest;
        private final NotificationRequest NotificatinRequest;
        private final WishRequest WishRequest;
        private final ItemRequest ItemRequest;
        private final FriendRequest FriendRequest; 

        public ClientHandler(Socket socket, Gson gson) {
            this.clientSocket = socket;
            this.gson = gson;
            this.UserRequest = new UserRequest(gson);
            this.NotificatinRequest = new NotificationRequest(gson);
            this.WishRequest = new WishRequest(gson);
            this.ItemRequest = new ItemRequest(gson);
            this.FriendRequest = new FriendRequest(gson);    
        }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String receivedJson;
            while ((receivedJson = in.readLine()) != null) {
//                System.out.println("Received JSON string: " + receivedJson);
                JsonObject jsonObject = JsonParser.parseString(receivedJson).getAsJsonObject();

                String requestType = jsonObject.has("type") ? jsonObject.get("type").getAsString() : "";
                jsonObject.remove("type");

                switch (requestType) {
                    case "User":
                        UserRequest.handleGetRequest(jsonObject, out, clientSocket);
                        break;
                    case "SignUp":
                        UserRequest.handleSignUpRequest(jsonObject, out, clientSocket);
                        break;
                    case "LogIn":
                        UserRequest.handleLogInRequest(jsonObject, out, clientSocket);
                        break;
                    case "Notification":
                        NotificatinRequest.handleNotificationRequest(jsonObject, out, clientSocket);
                        break;
                    case "Points":
                        UserRequest.handleChargeRequest(jsonObject, out, clientSocket);
                        break;
                    case "Wish":
                        WishRequest.handleGetWishRequest(jsonObject, out, clientSocket);
                        break;
                    case "RemoveWish":
                        WishRequest.handleRemoveRequest(jsonObject, out, clientSocket);
                        break;
                    case "getFriendWishes":
                        UserRequest.handleGetFriendWishRequest(jsonObject, out, clientSocket);
                        break;
                    case "GetFriends":
                        FriendRequest.handleGetFriendsRequest(jsonObject, out,clientSocket);
                        break;
                    case "GetFriendRequests":
                        FriendRequest.handleGetFriendRequestsRequest(jsonObject, out,clientSocket);
                        break;   
                    case "AddFriend":
                        FriendRequest.handleAddRequest(jsonObject, out, clientSocket);
                        break;
                    case "RemoveFriend":
                        FriendRequest.handleRemoveRequest(jsonObject, out, clientSocket);
                        break;
                    case "AcceptFriend":
                        FriendRequest.handleAcceptRequest(jsonObject, out, clientSocket);
                        break;
                    case "RejectFriend":
                        FriendRequest.handleRejectRequest(jsonObject, out, clientSocket);
                        break;
                    case "GetItems":
                        ItemRequest.handleGetItemRequest(jsonObject, out, clientSocket);
                        break;
                    case "AddWish":
                        WishRequest.handleAddRequest(jsonObject, out, clientSocket);
                        break;
                    case "Contribute":
                        WishRequest.handleContributeRequest(jsonObject, out, clientSocket);
                        break;
                        
                    case "LogOut":
                        out.println("{\"message\": \"Logged out successfully\"}");
                        out.flush();  
                        clientSocket.close();
                        break;
                    default:
                        out.println("{\"error\": \"Unknown request type\"}");
                        break;
                }
            }
            } catch (SocketException e) {
                System.err.println("Client disconnected: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (!clientSocket.isClosed()) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}