package server;

import server.database.*;
import server.parser.JSONTOEntryParser;
import server.parser.ResultToJSONParser;
import util.Entry;
import util.Result;

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
                        server.close();
                        break;
                    }

                    // Handle invalid type
                    if (!entry.getType().equals("get") && !entry.getType().equals("set") && !entry.getType().equals("delete")) {
                        Result result = new Result();
                        result.setResponse("Invalid input");
                        ResultToJSONParser resultToJSONParser = new ResultToJSONParser(result);
                        output.writeUTF(resultToJSONParser.parse());
                        continue;
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

                    controller.setCommand(command);
                    controller.executeCommand();

                    // Serialize result into JSON string
                    ResultToJSONParser resultToJSONParser = new ResultToJSONParser(result);
                    output.writeUTF(resultToJSONParser.parse());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}