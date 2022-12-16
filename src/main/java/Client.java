
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {

    private Socket clientSocket;

    private String serverAAddress = null;
    private String serverBAddress = null;

    private int filesNumberServerA = 0;
    private int filesNumberServerB = 0;

    private void log(String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " " + message);
    }

    public String toString() {
        return "Client with: \n"
                + "server A address: " + serverAAddress + "\n"
                + "server B address: " + serverBAddress + "\n"
                + "retrieve " + filesNumberServerA + " from server A \n"
                + "retrieve " + filesNumberServerB + " from server B \n";
    }

    public Client(String serverAAddress, String serverBAddress, int filesNumberServerA, int filesNumberServerB) {
        this.serverAAddress = serverAAddress;
        this.serverBAddress = serverBAddress;
        this.filesNumberServerA = filesNumberServerA;
        this.filesNumberServerB = filesNumberServerB;
        log(this.toString());
    }
}
