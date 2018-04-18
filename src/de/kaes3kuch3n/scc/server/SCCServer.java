package de.kaes3kuch3n.scc.server;

public class SCCServer {

    public static void main(String[] args) {
        if (args.length != 1)
            System.err.println("Usage: java -cp SimpleConsoleChat.jar de.kaes3kuch3n.scc.server.SCCServer <port>");

        new Server(Integer.parseInt(args[0]));
    }

}
