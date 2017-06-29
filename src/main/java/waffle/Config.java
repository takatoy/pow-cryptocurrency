package waffle;

public class Config {
    private static int listenPort = -1;
    private static String peerHostname = "";
    private static int peerPort = -1;
    private static boolean isMining = false;

    public static void setConfigData(int listenPort, String peerHostname, int peerPort, boolean isMining) {
    	Config.listenPort = listenPort;
    	Config.peerHostname = peerHostname;
    	Config.peerPort = peerPort;
    	Config.isMining = isMining;
    }
}
