package wishclient;

import java.io.*;
import java.net.*;

public class WishClientHandler {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

public WishClientHandler(String serverAddress, int port) throws IOException {
    try {
        socket = new Socket(serverAddress, port);
        socket.setSoTimeout(10000); // 10 seconds timeout for reading response
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
        // Clean up resources if initialization fails
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println("Error closing socket: " + ex.getMessage());
            }
        }
        throw new IOException("Failed to connect to the server at " + serverAddress + ":" + port, e);
    }
}


public void sendRequest(String request) throws IOException {
    if (out == null) {
        throw new IOException("Output stream is not initialized.");
    }
    if (!isConnected()) {
        throw new IOException("Not connected to the server.");
    }
    out.println(request.trim()); // Ensure newline is sent
    out.flush();
}

public String receiveResponse() throws IOException {
    try {
        String response = in.readLine();
        if (response == null) {
            throw new IOException("Server has closed the connection.");
        }
        return response;
    } catch (IOException e) {
        closeConnection(); // Close client socket if server shuts down
        throw e;
    }
}

public void closeConnection() {
    try {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            System.out.println("Disconnected from server.");
        }
    } catch (IOException e) {
        System.err.println("Error closing client socket: " + e.getMessage());
    }
}

    

    public void disconnect() throws IOException {
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            throw new IOException("Error closing input stream.", e);
        }

        if (out != null) {
            out.close();
        }

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            throw new IOException("Error closing socket.", e);
        }
    }


public boolean isConnected() {
    try {
        if (socket != null && socket.isConnected() && !socket.isClosed()) {
            return true;
        }
        return false;
    } catch (Exception e) {
        return false;
    }
}
}
