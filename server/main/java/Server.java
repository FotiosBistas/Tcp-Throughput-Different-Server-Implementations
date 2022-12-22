
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    // Path to the files
    private String file_path = ".\\files\\";

    private String ip;
    private int servicePort;
    private ServerSocket serverSocket;

    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);;

    /**
     * Prints a log with a timestamp to the left of it.
     * 
     * @param message the message that is going to be printed along with the
     *                timestamp.
     */
    private void log(String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " " + message);
    }

    // Constructor with specified path to the files
    public Server(String ip, int servicePort) {
        this.ip = ip;
        this.servicePort = servicePort;

    }

    private void startServer() {
        // This method just creates the socket that the server will listen to for
        // requests
        try {
            this.serverSocket = new ServerSocket(this.servicePort);
            log("listening for requests at port " + this.servicePort + "...");
        } catch (IOException e) {
            e.printStackTrace();
            log("Error setting up the main.java.Server.");
        }

        // Loop that accepts connections and creates a thread to
        // TODO: may end up making an implementation with threads.
        try {
            while (!serverSocket.isClosed()) {
                Socket clientConnection = serverSocket.accept();
                log("Received new connection from: " + clientConnection.getInetAddress() + " at port: "
                        + this.servicePort);

                RequestHandler requestHandler = new RequestHandler(clientConnection);

                Thread newThread = new Thread(requestHandler);
                newThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("Error trying to accept a connection.");
            // Close the server socket.
            shutdown();
        }
    }

    private void shutdown() {
        try {
            if (!serverSocket.isClosed()) {
                serverSocket.close();
                log("Successfully shutdown server");
            }
        } catch (IOException e) {
            e.printStackTrace();
            log("Error closing the server Socket.");
        }
    }

    public static void main(String[] args) {
        // The port & ip address will be provided as command line arguments.
        // If a port number is not provided, port 5096 will be used by default.
        if (args.length > 2) {
            System.err.println("Too many arguments provided.");
            System.err.println("Valid formats: {IP address, port number} or {IP address}");
        }
        if (args.length == 0) {
            // System.err.println("Too few arguments provided.");
            System.err.println("You must at least provide an IP address");
        }
        try {
            if (args.length == 1) {
                String ip = args[0];
                Server server = new Server(ip, 5096);
                server.startServer();
            } else if (args.length == 2) {
                String ip = args[0];
                int port = Integer.parseInt(args[1]);
                Server server = new Server(ip, port);
                server.startServer();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println("Invalid arguments format.");
            System.err.println("Valid formats: {IP address, port number} or {IP address}");
        }
    }
}
