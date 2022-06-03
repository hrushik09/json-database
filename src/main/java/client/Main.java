package client;

import client.cliParser.CommandArgs;
import com.beust.jcommander.JCommander;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Main {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Client started!");

            CommandArgs commandArgs = new CommandArgs();
            JCommander.newBuilder()
                    .addObject(commandArgs)
                    .build()
                    .parse(args);

            String sentMessage = commandArgs.generateMessage();
            output.writeUTF(sentMessage);
            System.out.println("Sent: " + sentMessage);

            String receivedMessage = input.readUTF();
            System.out.println("Received: " + receivedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}