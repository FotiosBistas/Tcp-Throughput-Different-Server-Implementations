import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class RequestHandler implements Runnable {

    Socket connection = null;
    ObjectInputStream objectInputStream = null;
    ObjectOutputStream objectOutputStream = null;

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

    public RequestHandler(Socket connection, ObjectInputStream localObjectInputStream,
            ObjectOutputStream localObjectOutputStream) {

        this.connection = connection;
        this.objectInputStream = localObjectInputStream;
        this.objectOutputStream = localObjectOutputStream;
    }

    @Override
    public void run() {
        log("Create new request handler to handle connection from: " + this.connection.getInetAddress());
        try {
            JSONObject json = (JSONObject) objectInputStream.readObject();
            log(json.toString());
        } catch (Exception e) {
            log("Exception:" + e + "occured while receiving json object");
        }
    }

}
