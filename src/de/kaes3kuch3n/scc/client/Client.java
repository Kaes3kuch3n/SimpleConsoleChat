package de.kaes3kuch3n.scc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class Client {

    private Socket connection;

    private PrintWriter output;
    private BufferedReader input;

    Client(String serverAddress, int port) {
        run(serverAddress, port);
    }

    private void run(String serverAddress, int port) {
        try {
            connectToServer(serverAddress, port);
            setupStreams();
            chatting();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                closeConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connectToServer(String serverAddress, int port) throws IOException {
        System.out.println("Connecting to the server...");
        connection = new Socket(serverAddress, port);
    }

    private void setupStreams() throws IOException {
        output = new PrintWriter(connection.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        System.out.println("Connected!");
    }

    private void chatting() throws IOException {
        String message;

        new Thread() {
            Scanner scanner = new Scanner(System.in);

            @Override
            public void run() {
                String messageToSend;
                do {
                    messageToSend = scanner.nextLine();
                    sendMessage(messageToSend);
                } while (!messageToSend.equals("quit"));
            }
        }.start();

        while ((message = input.readLine()) != null) {
            System.out.println("Server: " + message);
        }
    }

    private void sendMessage(String message) {
        output.println(message);
        System.out.println("Client: " + message);
    }

    private void closeConnection() throws IOException {
        input.close();
        output.close();
        connection.close();
    }

}
