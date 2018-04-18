package de.kaes3kuch3n.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class Server {

    private ServerSocket listener;
    private Socket connection;

    private PrintWriter output;
    private BufferedReader input;

    Server(int port) {
        try {
            listener = new ServerSocket(port);
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() {
        while (true) {
            try {
                waitForConnection();
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
    }

    private void waitForConnection() throws IOException {
        System.out.println("Waiting for connection...");
        connection = listener.accept();
        System.out.println("Connecing to " + connection.getInetAddress().getHostName() + "...");
    }

    private void setupStreams() throws IOException {
        output = new PrintWriter(connection.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        System.out.println("Connected!");
    }

    private void chatting() throws IOException {
        String message = "You are connected!";
        sendMessage(message);

        new Thread() {
            Scanner scanner = new Scanner(System.in);

            @Override
            public void run() {
                while (true) {
                    sendMessage(scanner.nextLine());
                }
            }
        }.start();

        while (!(message = input.readLine()).equals("quit")) {
            System.out.println("Client: " + message);
        }
    }

    private void sendMessage(String message) {
        output.println(message);
        System.out.println("Server: " + message);
    }

    private void closeConnection() throws IOException {
        input.close();
        output.close();
        connection.close();
    }

}
