package wishclint;

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
        throw new IOException("Failed to connect to the server at " + serverAddress + ":" + port, e);
    }
}


    public void sendRequest(String request) throws IOException {
        if (out != null) {
            out.println(request); 
        } else {
            throw new IOException("Output stream is not initialized.");
        }
    }


    public String receiveResponse() throws IOException {
        try {
            if (in != null) {
                return in.readLine();  
            } else {
                throw new IOException("Input stream is not initialized.");
            }
        } catch (SocketException e) {
            throw new IOException("Connection to the server was lost: " + e.getMessage(), e);
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
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
