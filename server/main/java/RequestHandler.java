import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestHandler implements Runnable {

    Socket connection = null;
    ObjectInputStream objectInputStream = null;
    ObjectOutputStream objectOutputStream = null;
    FileInputStream fileInputStream = null;

    private final String file_extension = ".m4s";

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

    public RequestHandler(Socket connection) {

        this.connection = connection;
        try {
            objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
            objectInputStream = new ObjectInputStream(connection.getInputStream());
        } catch (Exception e) {
            log("Exception: " + e + " while trying to create request handler");
        }
        log("Created new request handler");
    }

    /**
     * Filenames are received in the form: String(["s001.ms", "s002.ms"]).
     * Removes the commas the brackets and the quotes from the string.
     * 
     * @param filenames
     * @return
     */
    private String[] parseToStringArray(String filenames) {
        filenames = filenames.replace("[", "");
        filenames = filenames.replace("]", "");
        filenames = filenames.replace("\"", "");
        log("Turned received json array to: " + filenames);
        return filenames.split(",");
    }

    @Override
    public void run() {
        log("Handling connection from: " + this.connection.getInetAddress());
        try {
            String string_json = (String) objectInputStream.readObject();
            JSONObject json = new JSONObject(string_json);
            JSONArray filenames = ((JSONArray) json.get("Filenames"));
            String[] workable = parseToStringArray(filenames.toString());

            Arrays.stream(workable)
                    .forEach(filename -> {
                        String file = filename + this.file_extension;
                        try {
                            fileInputStream = new FileInputStream(
                                    "..\\files\\"
                                            + file);

                        } catch (Exception e) {
                            log("Exception: " + e + " occured while reading file: " + file);
                        }
                    });

        } catch (Exception e) {
            log("Exception:" + e + "occured while receiving json object");
        }
    }

}
