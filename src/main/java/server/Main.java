package server;

import server.database.Controller;
import server.database.Database;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static final int PORT = 9999;
    static boolean exit = false;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server started!");

            Controller controller = new Controller();
            Database database = new Database();
            ExecutorService executor = Executors.newFixedThreadPool(5);

            while (!exit) {
                ClientHandler clientHandler = new ClientHandler(server.accept(), controller, database);
                Future<Boolean> future = executor.submit(clientHandler);
                exit = future.get();
            }

            executor.shutdown();
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}