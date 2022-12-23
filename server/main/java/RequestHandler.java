import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.StandardJavaFileManager.PathFactory;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestHandler implements Runnable {

    private Socket connection = null;
    private DataInputStream dataInputStream = null;
    private DataOutputStream dataOutputStream = null;
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
            dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataInputStream = new DataInputStream(connection.getInputStream());
        } catch (Exception e) {
            log("Exception: " + e + " while trying to create request handler");
        }
        log("Created new request handler");
    }

    /**
     * Filenames are received in the form: String(["s001.ms", "s002.ms"]).
     * Removes the commas the brackets and the quotes from the string.
     * 
     * @param filenames the filenames json array turned toString()
     * @return all the filenames to read
     */
    private String[] parseToStringArray(String filenames) {
        filenames = filenames.replace("[", "");
        filenames = filenames.replace("]", "");
        filenames = filenames.replace("\"", "");
        filenames = filenames.replace("Out of range number was given or there aren't any more files", "");
        filenames = filenames.replace("Formated file number remained null after string format", "");
        log("Turned received json array to: " + filenames);
        return filenames.split(",");
    }

    @Override
    public void run() {
        log("Handling connection from: " + this.connection.getInetAddress());
        try {
            String string_json = dataInputStream.readUTF();
            JSONObject json = new JSONObject(string_json);
            JSONArray filenames = ((JSONArray) json.get("Filenames"));
            String[] workable = parseToStringArray(filenames.toString());

            Arrays.stream(workable)
                    .forEach(filename -> {
                        String path = "..\\files\\" + filename + this.file_extension;
                        sendFile(path);
                    });
            log("Finished sending files to the client");
        } catch (Exception e) {
            log("Exception:" + e + "occured while receiving json object");
        }
    }

    private void sendFile(String path) {
        try {
            File file = new File(path);
            fileInputStream = new FileInputStream(file);
            // send the file name
            log("Sending new file to client: " + file.getName());
            dataOutputStream.writeUTF(file.getName());

            // send the file size
            log("Sending file length: " + file.length() + " to client");
            dataOutputStream.writeLong(file.length());
            // chunking the file
            int bytes = 0;
            byte[] buffer = new byte[4 * 1024];
            while ((bytes = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytes);
                dataOutputStream.flush();
            }
            fileInputStream.close();
        } catch (Exception e) {
            log("Exception: " + e + " occured while reading or the sending the file: " + path);
        }
    }

}
