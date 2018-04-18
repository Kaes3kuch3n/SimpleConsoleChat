package de.kaes3kuch3n.scc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Connection extends Thread {

    private Server server;
    private Socket connection;

    private PrintWriter output;
    private BufferedReader input;

    Connection(Server server, Socket socket) {
        this.server = server;
        this.connection = socket;
    }

    @Override
    public void run() {
        try {
            setupConnection();
            processMessages();
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

    private void setupConnection() throws IOException {
        output = new PrintWriter(connection.getOutputStream(), true);
        server.addConnection(output);
        input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    }

    private void processMessages() throws IOException {
        String message;

        while (!(message = input.readLine()).equals("/quit")) {
            server.sendMessage(message);
        }
    }

    private void closeConnection() throws IOException {
        server.removeConnection(output);
        output.close();
        input.close();
        connection.close();
    }

}
