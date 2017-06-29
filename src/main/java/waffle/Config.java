package waffle;

public class Config {
    private static int listenPort = -1;
    private static String peerHostName = "";
    private static int peerPort = -1;
    private static boolean isMining = false;

    public static void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }
    public static void setPeerHostName(String peerHostName) {
        this.peerHostName = peerHostName;
    }
    public static void setPeerPort(int peerPort) {
        this.peerPort = peerPort;
    }
    public static void setIsMining(boolean isMining) {
        this.isMining = isMining;
    }
}
