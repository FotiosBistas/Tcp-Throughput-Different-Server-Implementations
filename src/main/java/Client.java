import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

public class Client {

    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;

    private Socket clientSocket;

    private String serverAAddress = null;
    private String serverBAddress = null;
    private int portA = 0;
    private int portB = 0;

    private int filesNumberServerA = 0;
    private int filesNumberServerB = 0;
    private int latestFileReceived = 0;

    private void log(String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " " + message);
    }

    public String toString() {
        return "\nClient with: \n"
                + "server A address: " + serverAAddress + "\n"
                + "server B address: " + serverBAddress + "\n"
                + "request analogy " + filesNumberServerA + " from server A \n"
                + "request analogy " + filesNumberServerB + " from server B \n"
                + "server A port: " + portA + "\n"
                + "server B port: " + portB + "\n";
    }

    private void createRequest(String serverIP, int port, int filenumber) {
        log("Creating new request");

        JSONObject nJsonObject = new JSONObject();
        String fileRequest = null;

        try {
            fileRequest = createFileNameFromNumber(filesNumberServerA);
        } catch (Throwable e) {
            log("Error while trying to create filename: " + e);
            return;
        }

        nJsonObject.put("ServerIP", serverIP);
        nJsonObject.put("ServerPort", port);
        nJsonObject.put("Filename", fileRequest);

        log("Created new JSON object: " + nJsonObject);
    }

    private String createFileNameFromNumber(int number) {
        String formatted = null;

        if (number < 1 || number > 999) {
            throw new Error("Out of range number was given");
        }

        formatted = String.format("%03d", number);

        if (formatted == null) {
            throw new Error("Formated file number remained null after string format");
        }

        return "s" + formatted;
    }

    public Client(String serverAAddress, String serverBAddress, int filesNumberServerA, int filesNumberServerB,
            int portA, int portB) {

        this.serverAAddress = serverAAddress;
        this.serverBAddress = serverBAddress;
        this.filesNumberServerA = filesNumberServerA;
        this.filesNumberServerB = filesNumberServerB;
        this.portA = portA;
        this.portB = portB;

        log(this.toString());
    }
}
