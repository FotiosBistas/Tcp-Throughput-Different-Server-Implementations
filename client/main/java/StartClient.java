import java.util.ArrayList;
import java.util.List;

public class StartClient {

    private Long nanoSecondsToSeconds(Long nanoseconds) {
        return nanoseconds / 1000000000;
    }

    private Long nanoSecondsToMilliSeconds(Long nanoseconds) {
        return nanoseconds / 1000000;
    }

    private static final String helpText = "CLIENT TOOL \n"
            + "CLIENT MUST BE PROVIDED WITH 6 POSITIONAL ARGUMENTS \n"
            + "-SERVER A IP\n"
            + "-SERVER B IP\n"
            + "-SERVER A PORT\n"
            + "-SERVER B PORT\n"
            + "-NUMBER OF FILES TO REQUEST FROM SERVER A\n"
            + "-NUMBER OF FILES TO REQUEST FROM SERVER B\n";

    public static void main(String[] Args) {
        if (Args.length < 6 || Args.length > 6) {
            System.out.println(helpText);
            return;
        }
        Client client = new Client(Args[0], Args[1], Integer.parseInt(Args[2]), Integer.parseInt(Args[3]),
                Integer.parseInt(Args[4]), Integer.parseInt(Args[5]));

        ArrayList<ArrayList<Long>> metrics = client.StartRequesting();

        for (ArrayList<Long> metric : metrics) {
            System.out.println(metric);
        }
    }
}