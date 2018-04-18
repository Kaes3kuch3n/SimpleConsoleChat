package de.kaes3kuch3n.scc.client;

public class SCCClient {

    public static void main(String[] args) {
        if (args.length != 2)
            System.err.println("Usage: java -jar SimpleConsoleChat.jar <server address> <port>");

        new Client(args[0], Integer.parseInt(args[1]));
    }

}
