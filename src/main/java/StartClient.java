
public class StartClient {

    private static final String helpText = "CLIENT TOOL \n"
            + "CLIENT MUST BE PROVIDED WITH 3 POSITIONAL ARGUMENTS \n"
            + "-SERVER IP\n"
            + "-NUMBER OF FILES TO REQUEST EACH TIME FROM SERVER \n"
            + "-SERVER PORT\n"
            + "-STEP IN FILE REQUEST";

    public static void main(String[] Args) {
        if (Args.length < 4 || Args.length > 4) {
            System.out.println(helpText);
            return;
        }
        Client client = new Client(Args[0], Integer.parseInt(Args[1]), Integer.parseInt(Args[2]),
                Integer.parseInt(Args[3]));

        client.StartRequesting();
    }
}