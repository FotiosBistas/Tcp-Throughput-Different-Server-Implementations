
public class StartClient {

    private static final String helpText = "CLIENT TOOL \n"
            + "CLIENT MUST BE PROVIDED WITH FOUR POSITIONAL ARGUMENTS \n"
            + "-NUMBER OF FILES TO RETRIEVE FROM SERVER A\n"
            + "-NUMBER OF FILES TO RETRIEVE FROM SERVER B\n"
            + "-SERVER A IP\n"
            + "-SERVER B IP\n";

    public static void main(String[] Args) {
        if (Args.length < 4) {
            System.out.println(helpText);
            return;
        }
        Client client = new Client(Args[0], Args[1], Integer.parseInt(Args[2]), Integer.parseInt(Args[3]));
    }
}