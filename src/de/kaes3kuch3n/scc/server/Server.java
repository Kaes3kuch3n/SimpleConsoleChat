package de.kaes3kuch3n.scc.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Scanner;

class Server {

    private ServerSocket listener;
    private HashSet<PrintWriter> outputs;

    Server(int port) {
        outputs = new HashSet<>();

        try {
            listener = new ServerSocket(port);
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run() {
        try {
            startServerMessageThread();
            waitForConnections();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startServerMessageThread() {
        new Thread() {
            Scanner scanner = new Scanner(System.in);

            @Override
            public void run() {
                while (true) {
                    sendMessage(scanner.nextLine(), true);
                }
            }
        }.start();
    }

    private void waitForConnections() throws IOException {
        System.out.println("Waiting for connection...");
        while (true) {
            new Connection(this, listener.accept()).start();
        }
    }

    void sendMessage(String message) {
        sendMessage(message, false);
    }

    private void sendMessage(String message, boolean isServer) {
        message = (isServer) ? "Server: " + message : "Client: " + message;

        for (PrintWriter output : outputs) {
            output.println(message);
        }

        System.out.println(message);
    }

    void addConnection(PrintWriter output) {
        outputs.add(output);
    }

    void removeConnection(PrintWriter output) {
        outputs.remove(output);
    }

}
