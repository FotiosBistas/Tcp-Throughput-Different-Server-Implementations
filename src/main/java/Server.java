package main.java;

import java.io.*;
import java.net.*;

public class Server {

    // Path to the files
    private String file_path = "\\files\\";

    private String ip;
    private int servicePort;
    private ServerSocket serverSocket;
    private Socket clientConnection;

    // Constructor with specified path to the files
    public Server(String ip, int servicePort){
        this.ip = ip;
        this.servicePort = servicePort;

    }

    private void startServer(){
        // This method just creates the socket that the server will listen to for requests
        try {
            this.serverSocket = new ServerSocket(this.servicePort);
            System.out.println("listening for requests at port "+this.servicePort+"...");
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error setting up the main.java.Server.");
        }

        // Loop that accepts connections and creates a thread to
        // TODO: may end up making an implementation with threads.
        try {
            while (!serverSocket.isClosed()){
                clientConnection = serverSocket.accept();
                /* TODO: Here we can do one of the two:
                    1) Create a handler Thread that takes care of the requests,
                    2) The server switches from accepting connections to handling requests
                        from the established connection.
                    (note) The later is simpler, and since we only ever accept one connection,
                     handler threads are not really necessary. It may be a better practice though.
                 */
            }
        }catch (IOException e){
            e.printStackTrace();
            System.err.println("Error trying to accept a connection.");
            // Close the server socket.
            shutdown();
        }
    }

    private void shutdown(){
        try {
            if(!serverSocket.isClosed()){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
            System.err.println("Error closing the server Socket.");
        }
    }

    public static void main(String[] args) {
        // The port & ip address will be provided as command line arguments.
        // If a port number is not provided, port 5096 will be used by default.
        if (args.length > 2){
            System.err.println("Too many arguments provided.");
            System.err.println("Valid formats: {IP address, port number} or {IP address}");
        }
        if (args.length == 0){
            //System.err.println("Too few arguments provided.");
            System.err.println("You must at least provide an IP address");
        }
        try {
            if (args.length == 1){
                String ip = args[0];
                Server server = new Server(ip, 5096);
                server.startServer();
            }else if (args.length == 2){
                String ip = args[0];
                int port = Integer.parseInt(args[1]);
                Server server = new Server(ip, port);
                server.startServer();
            }
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            System.err.println("Invalid arguments format.");
            System.err.println("Valid formats: {IP address, port number} or {IP address}");
        }
    }
}

