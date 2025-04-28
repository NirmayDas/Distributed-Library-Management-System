package com.nirmaydas.serverside;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private static final int PORT = 5000;
    private final Catalog catalog;

    public Server() {
        this.catalog = new Catalog();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            System.out.println("Loaded items:");
            catalog.getItems().forEach((id, item) -> System.out.println(item));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                ClientHandler clientHandler = new ClientHandler(clientSocket, catalog);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        // String catalogFile = "src/serverside/catalog.txt"; //(temporarily removing for mongo test)
        Server server = new Server();
        server.start();
    }
}