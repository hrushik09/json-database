/**
 * Handles a new client.
 * Implements Callable so call() method can return false in case of "exit" command.
 */

package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import server.database.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ClientHandler implements Callable<Boolean> {
    private final Socket socket;
    private final Controller controller;
    private final Database database;

    public ClientHandler(Socket socket, Controller controller, Database database) {
        this.socket = socket;
        this.controller = controller;
        this.database = database;
    }

    @Override
    public Boolean call() {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            // Parse incoming JSON to JsonObject
            String receivedMessage = input.readUTF();
            JsonObject jsonObject = new Gson().fromJson(receivedMessage, JsonObject.class);

            // Handle exit case
            if (jsonObject.get("type").getAsString().equals("exit")) {
                String s = "{\"response\":\"OK\"}";
                output.writeUTF(s);
                socket.close();

                // need to stop server as "exit" command is invoked
                return true;
            }

            // Handle invalid type
            if (!jsonObject.get("type").getAsString().equals("get")
                    && !jsonObject.get("type").getAsString().equals("set")
                    && !jsonObject.get("type").getAsString().equals("delete")) {
                String s = "{\"response\":\"Invalid input\"}";
                output.writeUTF(s);
                socket.close();

                // no need to stop server here
                return false;
            }

            Command command;
            switch (jsonObject.get("type").getAsString()) {
                case "get":
                    command = new GetCommand(database, jsonObject);
                    break;

                case "set":
                    command = new SetCommand(database, jsonObject);
                    break;

                case "delete":
                    command = new DeleteCommand(database, jsonObject);
                    break;

                default:
                    command = null;
                    break;
            }

            // set and execute command
            controller.setCommand(command);
            String s = controller.executeCommand();
            output.writeUTF(s);

            // Each client can send only one request
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // no need to stop server here either
        return false;
    }
}