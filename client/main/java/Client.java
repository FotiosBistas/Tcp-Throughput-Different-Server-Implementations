import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.spec.ECField;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.stream.IntStream;
import org.json.JSONArray;
import org.json.JSONObject;

public class Client {

    private DataOutputStream dataAOutputStream = null;
    private DataInputStream dataAInputStream = null;
    private DataOutputStream dataBOutputStream = null;
    private DataInputStream dataBInputStream = null;
    private Socket clientASocket;
    private Socket clientBSocket;

    private String serverAAddress = "";
    private String serverBAddress = "";

    private int portA = 0;
    private int portB = 0;

    ArrayList<ArrayList<Long>> metrics = new ArrayList<>();

    // files to request from A server
    private int filesANumberServer = 0;
    // files to request from B server
    private int filesBNumberServer = 0;
    // step to take when changing the file range. This is essentially what the other
    // client will ask.
    // the next file we are expecting.
    private volatile int nextAfile = 1;
    private volatile int nextBfile = 1;

    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

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
                + "server A address: " + serverAAddress + "\n"
                + "server B address: " + serverAAddress + "\n"
                + "request analogy A " + filesANumberServer + "\n"
                + "request analogy B " + filesBNumberServer + "\n"
                + "server A port: " + portA + "\n"
                + "server B port: " + portB + "\n"
                + "next file expected from A initialization: " + nextAfile + "\n"
                + "next file expected from B initialization: " + nextBfile + "\n";
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

    public Client(String serverAAddress, String serverBAddress, int portA, int portB, int filesANumberServer,
            int filesBNumberServer) {

        this.serverAAddress = serverAAddress;
        this.serverBAddress = serverBAddress;

        this.portA = portA;
        this.portB = portB;

        this.filesANumberServer = filesANumberServer;
        this.filesBNumberServer = filesBNumberServer;

        this.nextBfile = filesANumberServer + 1;
        log(this.toString());

        try {
            clientASocket = new Socket(serverAAddress, portA);
            clientBSocket = new Socket(serverBAddress, portB);
            // read and send json objects
            dataAOutputStream = new DataOutputStream(clientASocket.getOutputStream());
            dataAInputStream = new DataInputStream(clientASocket.getInputStream());
            dataBOutputStream = new DataOutputStream(clientBSocket.getOutputStream());
            dataBInputStream = new DataInputStream(clientBSocket.getInputStream());

        } catch (Exception e) {
            log("Exception occured while trying to create client: " + e);
            return;
        }
        log("Succesfully connected to server");

    }

    /**
     * Adds the step into the next file to request the next file from the
     * corresponding server.
     */
    private void addAStep(int step) {
        this.nextAfile += step + 1;
        log("Step was: " + step + " and next file to be expected from B: " + nextAfile);
    }

    /**
     * Adds the step into the next file to request the next file from the
     * corresponding server.
     */
    private void addBStep(int step) {
        this.nextBfile += step + 1;
        log("Step was: " + step + " and next file to be expected from B: " + nextBfile);
    }

    /**
     * Creates a new number number array inside the range.
     * 
     * @param start the start of the number array.
     * @param to    the end of the number array.
     * @return Returns the number array created.
     */
    private synchronized int[] createNumberArray(int start, int to, String aSteporBStep) {
        int[] numberArray = IntStream.rangeClosed(start, to).toArray();
        log("Creating number array... File number A: " + filesANumberServer + " File Number B: " + filesBNumberServer);
        if (aSteporBStep.equals("A")) {
            nextAfile = to;
            addAStep(filesBNumberServer);
        } else {
            nextBfile = to;
            addBStep(filesANumberServer);
        }
        return numberArray;
    }

    /**
     * Call the function to create a new JSON object which is the request. It also
     * tweaks the necessary variables so the clients can ask for the next set of
     * files from the server.
     * 
     * @param serverAddress     The server address we are creating the request for.
     * @param port              The port of the server.
     * @param nextfile          The next file we are awaiting.
     * @param filesNumberServer The number of files we ask in each request.
     * @param aSteporBStep      Choose what client variables we modify. These
     *                          differentiate from A and B.
     * @return returns the JSON object.
     */
    private JSONObject createRequest(String serverAddress, int port, int nextfile, int filesNumberServer,
            String aSteporBStep) {

        JSONObject requestforJSON = createRequestObject(serverAddress, port,
                createNumberArray(nextfile, nextfile + filesNumberServer - 1, aSteporBStep));
        log("Created new request for server: " + aSteporBStep);

        return requestforJSON;

    }

    private void receiveFile(DataInputStream dataInputStream, int files_expected) {
        int counter = 0;
        log("Files expected in receive file: " + files_expected + " and counter is initiliazed: " + counter);
        while (true) {
            try {
                // receive filename from connection
                String filename = dataInputStream.readUTF();
                log("Read: " + filename + " filename from server");
                // receive file size from connection
                long filesize = dataInputStream.readLong();
                log("Read: " + filesize + " filesize from server");
                // create new file if it doesn't exist
                File new_file = new File("..\\received_files\\" + filename);
                new_file.createNewFile();
                FileOutputStream fos = new FileOutputStream(new_file);
                int bytes = 0;
                byte[] buffer = new byte[4 * 1024];
                while (filesize > 0
                        && (bytes = dataInputStream.read(buffer, 0, (int) Math.min(buffer.length, filesize))) != -1) {
                    fos.write(buffer, 0, bytes);
                    filesize -= bytes; // read upto file size
                }
                fos.close();
            } catch (Exception e) {
                log("Exception: " + e + " occured in receive file.");
                break;
            }
            /*
             * counter = counter + 1;
             * log("Counter has become: " + counter);
             * if (counter >= files_expected) {
             * break;
             * }
             */
        }
    }

    /**
     * Starts requesting both servers for the files. It does this by creating two
     * callable tasks which are then called using invokeAll() to wait for their
     * completion. Inside these two tasks the time it takes for each request to
     * complete is counted in nanoseconds. After that using the get() method for
     * each Future<Object> we get the ArrayList<Long> of each request. Then to
     * return it we add the array list to another List.
     * 
     * @return returns the array of nanoseconds it took for each request
     */
    public ArrayList<ArrayList<Long>> StartRequesting() {

        Callable<Object> task1 = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                log("Starting to request from server A");
                // breaks with an if inside
                ArrayList<Long> requestTimes = new ArrayList<>();
                while (true) {
                    long startTime = System.nanoTime(); // get the start time in nanoseconds
                    JSONObject requestforJSON = createRequest(serverAAddress, portA, nextAfile, filesANumberServer,
                            "A");

                    dataAOutputStream.writeUTF((requestforJSON.toString()));
                    dataAOutputStream.flush();
                    receiveFile(dataAInputStream, filesANumberServer);
                    long endTime = System.nanoTime(); // get the end time in nanoseconds
                    long elapsedTime = endTime - startTime; // calculate the elapsed time in nanoseconds
                    log("It took: " + elapsedTime + " nanoseconds to complete the request");
                    requestTimes.add(elapsedTime);

                    // essentially the "Out of range number was given or there aren't any more
                    // files" acts as a done in the requests
                    // execute the code that you want to measure the time for
                    String[] array = (String[]) requestforJSON.get("Filenames");
                    if (Arrays.asList(array).contains("Out of range number was given or there aren't any more files")) {
                        log("Breaking from for A loop");
                        break;
                    }

                }
                log("Finished requesting from server A");
                return requestTimes;
            }
        };

        Callable<Object> task2 = new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                log("Starting to request from server B");
                ArrayList<Long> requestTimes = new ArrayList<>();
                while (true) {
                    long startTime = System.nanoTime(); // get the start time in nanoseconds

                    JSONObject requestforJSON = createRequest(serverBAddress, portB, nextBfile, filesBNumberServer,
                            "B");

                    dataBOutputStream.writeUTF((requestforJSON.toString()));
                    dataBOutputStream.flush();
                    receiveFile(dataBInputStream, filesBNumberServer);
                    long endTime = System.nanoTime(); // get the end time in nanoseconds
                    long elapsedTime = endTime - startTime; // calculate the elapsed time in nanoseconds
                    log("It took: " + elapsedTime + " nanoseconds to complete the request");
                    requestTimes.add(elapsedTime);

                    // essentially the "Out of range number was given or there aren't any more
                    // files" acts as a done in the requests
                    // execute the code that you want to measure the time for
                    String[] array = (String[]) requestforJSON.get("Filenames");
                    if (Arrays.asList(array).contains("Out of range number was given or there aren't any more files")) {
                        log("Breaking from B for loop");
                        break;
                    }

                }
                log("Finished requesting from server B");
                return requestTimes;
            }
        };

        // Create a list of tasks
        List<Callable<Object>> tasks = Arrays.asList(task1, task2);
        ArrayList<ArrayList<Long>> metrics = new ArrayList<ArrayList<Long>>();

        try {
            List<Future<Object>> results = executor.invokeAll(tasks);
            for (Future<Object> result : results) {
                ArrayList<Long> value = (ArrayList<Long>) result.get(); // get the result of the task
                metrics.add(value);
            }
            log("Finished requests");
        } catch (InterruptedException e) {
            log("Error while waiting for threads to finish and getting results: " + e);
        } catch (ExecutionException e) {
            log("Error while waiting for threads to finish and getting results " + e);
        }

        executor.shutdown();

        return metrics;
    }
}
