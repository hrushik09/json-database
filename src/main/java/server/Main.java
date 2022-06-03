package server;

import server.database.*;

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

            Controller controller = new Controller();
            Database database = new Database();

            while (true) {
                try (Socket socket = server.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                    String receivedMessage = input.readUTF();
                    String sentMessage;
                    if (receivedMessage.equals("exit")) {
                        sentMessage = "OK";
                        output.writeUTF(sentMessage);
                        server.close();
                        break;
                    }

                    String[] cmdArr = getUpdatedInput(receivedMessage);
                    if (cmdArr == null) {
                        sentMessage = "Invalid input";
                        output.writeUTF(sentMessage);
                        continue;
                    }
                    Command command;

                    switch (cmdArr[0]) {
                        case "get":
                            command = new GetCommand(database, cmdArr);
                            break;

                        case "set":
                            command = new SetCommand(database, cmdArr);
                            break;

                        case "delete":
                            command = new DeleteCommand(database, cmdArr);
                            break;

                        default:
                            command = null;
                            break;
                    }

                    controller.setCommand(command);
                    sentMessage = controller.executeCommand();

                    output.writeUTF(sentMessage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] getUpdatedInput(String input) {
        String[] arr;
        if (input.startsWith("get")) {
            arr = input.split(" ");
        } else if (input.startsWith("set")) {
            arr = input.replaceFirst(" ", "#").replaceFirst(" ", "#")
                    .split("#");
        } else if (input.startsWith("delete")) {
            arr = input.split(" ");
        } else {
            return null;
        }
        return arr;
    }
}