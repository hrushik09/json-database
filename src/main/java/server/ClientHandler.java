/**
 * Handles a new client.
 * Implements Callable so call() method can return false in case of "exit" command.
 */

package server;

import server.database.*;
import server.parser.JSONTOEntryParser;
import server.parser.ResultToJSONParser;
import util.Entry;
import util.Result;

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

            // Parse incoming JSON to an object of Entry class
            String receivedMessage = input.readUTF();
            JSONTOEntryParser jsontoEntryParser = new JSONTOEntryParser(receivedMessage);
            Entry entry = jsontoEntryParser.parse();

            // Handle exit case
            if (entry.getType().equals("exit")) {
                Result result = new Result();
                result.setResponse("OK");
                ResultToJSONParser resultToJSONParser = new ResultToJSONParser(result);
                output.writeUTF(resultToJSONParser.parse());
                socket.close();

                // need to stop server as "exit" command is invoked
                return true;
            }

            // Handle invalid type
            if (!entry.getType().equals("get") && !entry.getType().equals("set") && !entry.getType().equals("delete")) {
                Result result = new Result();
                result.setResponse("Invalid input");
                ResultToJSONParser resultToJSONParser = new ResultToJSONParser(result);
                output.writeUTF(resultToJSONParser.parse());
                socket.close();

                // no need to stop server here
                return false;
            }

            Command command;
            Result result = new Result();

            // entry contains command parameters
            // result will be populated with operation results
            switch (entry.getType()) {
                case "get":
                    command = new GetCommand(database, entry, result);
                    break;

                case "set":
                    command = new SetCommand(database, entry, result);
                    break;

                case "delete":
                    command = new DeleteCommand(database, entry, result);
                    break;

                default:
                    command = null;
                    break;
            }

            // set and execute command
            controller.setCommand(command);
            controller.executeCommand();

            // Serialize result into JSON string
            ResultToJSONParser resultToJSONParser = new ResultToJSONParser(result);
            output.writeUTF(resultToJSONParser.parse());

            // Each client can send only one request
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // no need to stop server here either
        return false;
    }
}