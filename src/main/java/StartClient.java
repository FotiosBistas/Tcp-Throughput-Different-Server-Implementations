
public class StartClient {

    private static final String helpText = "CLIENT TOOL \n"
            + "CLIENT MUST BE PROVIDED WITH FOUR POSITIONAL ARGUMENTS \n"
            + "-SERVER A IP\n"
            + "-SERVER B IP\n"
            + "-NUMBER OF FILES TO REQUEST EACH TIME FROM SERVER A\n"
            + "-NUMBER OF FILES TO REQUEST EACH TIME FROM SERVER B\n"
            + "-SERVER A PORT\n"
            + "-SERVER B PORT\n";

    public static void main(String[] Args) {
        if (Args.length < 6) {
            System.out.println(helpText);
            return;
        }
        Client client = new Client(Args[0], Args[1], Integer.parseInt(Args[2]), Integer.parseInt(Args[3]),
                Integer.parseInt(Args[4]), Integer.parseInt(Args[5]));
    }
}