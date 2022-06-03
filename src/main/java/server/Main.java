package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final int PORT = 9999;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");
            boolean connected = false;

            while (!connected) {
                try (Socket socket = server.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
                    connected = true;
                    String receivedMessage = input.readUTF();
                    System.out.println("Received: " + receivedMessage);

                    int lastIndex = receivedMessage.lastIndexOf(' ');
                    int num = Integer.parseInt(receivedMessage.substring(lastIndex + 1));
                    String sentMessage = String.format("A record # %d was sent!", num);
                    output.writeUTF(sentMessage);
                    System.out.println("Sent: " + sentMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}