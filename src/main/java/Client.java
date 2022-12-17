import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

public class Client {

    private String serverAddress = null;
    private int port = 0;

    // files to request from the server
    private int filesNumberServer = 0;
    // step to take when changing the file range. This is essentially what the other
    // client will ask.
    private int stepsinFileRequest = 0;
    // the next file we are expecting.
    private int nextfile = 1;

    ThreadPoolExecutor executor;

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

    public String toString() {
        return "\nClient with: \n"
                + "server address: " + serverAddress + "\n"
                + "request analogy " + filesNumberServer + " from server: " + serverAddress + "\n"
                + "server port: " + port + "\n"
                + "steps in file request: " + stepsinFileRequest;
    }

    /**
     * This function must be called each time after the client has received the
     * requested file/files. Creates a new JSON object which is a request object and
     * sends it to the server.
     * 
     * @param serverIP    the server we will connect to
     * @param port        the port that the server listens to
     * @param filenumbers the file numbers we want to generate filenames for.
     * @return returns the JSON object created.
     */
    private JSONObject createRequestObject(String serverIP, int port, int[] filenumbers) {
        log("Creating new request");

        JSONObject nJsonObject = new JSONObject();
        String[] fileRequests = createFileNameFromNumber(filenumbers);

        nJsonObject.put("ServerIP", serverIP);
        nJsonObject.put("ServerPort", port);
        nJsonObject.put("Filenames", fileRequests);

        log("Created new JSON object: " + nJsonObject);

        return nJsonObject;
    }

    /**
     * Creates a new file name of the following format:
     * sXXX where XXX starts from 001 and goes up to 160.
     * 
     * @param number the number of the file we want to request next
     * @return The formated file names or an error if an error occured.
     */
    private String[] createFileNameFromNumber(int[] number) {
        String[] files = Arrays.stream(number)
                .mapToObj((num) -> {

                    if (num < 1 || num > 160) {

                        return "Out of range number was given or there aren't any more files";
                    }

                    String formatted = String.format("%03d", num);

                    if (formatted == null) {
                        return "Formated file number remained null after string format";
                    }

                    return "s" + formatted;
                }).toArray(String[]::new);
        return files;
    }

    // TODO create a method that handles an array

    public Client(String serverAddress, int filesNumberServer,
            int port, int stepsinFileRequest) {

        this.serverAddress = serverAddress;
        this.filesNumberServer = filesNumberServer;
        this.port = port;
        this.stepsinFileRequest = stepsinFileRequest;
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        log(this.toString());
    }

    /**
     * Adds the step into the next file to request the next file from the
     * corresponding server.
     */
    private void addStep(int step) {
        this.nextfile += step + 1;
    }

    /**
     * Creates a new number number array inside the range.
     * 
     * @param start the start of the number array.
     * @param to    the end of the number array.
     * @return Returns the number array created.
     */
    private int[] createNumberArray(int start, int to) {
        int[] numberArray = IntStream.rangeClosed(start, to).toArray();
        nextfile = to;
        addStep(stepsinFileRequest);
        return numberArray;
    }

    /**
     * Starts requesting the files from the server. It stops when client reaches the
     * end of the requests.
     */
    public void StartRequesting() {
        // breaks with an if inside
        while (true) {
            JSONObject requestJSON = createRequestObject(serverAddress, port,
                    createNumberArray(nextfile, nextfile + filesNumberServer - 1));

            Request request = new Request(requestJSON);

            if (request.jsonObject == null) {
                continue;
            }

            Thread thread = new Thread(request);
            executor.submit(thread);

            // essentially the "Out of range number was given or there aren't any more
            // files" acts as a done in the requests
            String[] array = (String[]) requestJSON.get("Filenames");
            if (Arrays.asList(array).contains("Out of range number was given or there aren't any more files")) {
                break;
            }

        }

    }

    private class Request implements Runnable {
        private DataOutputStream dataOutputStream = null;
        private DataInputStream dataInputStream = null;
        private JSONObject jsonObject;
        private Socket clientSocket;

        Request(JSONObject jsonObject) {
            try {
                clientSocket = new Socket(serverAddress, port);
                dataInputStream = new DataInputStream(clientSocket.getInputStream());
                dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                this.jsonObject = jsonObject;
            } catch (Exception e) {
                log("Expection while trying to create socket: " + e);
            }
        }

        @Override
        public void run() {
            log("Started new thread");
            // TODO Auto-generated method stub

        }
    }
}
