package waffle;

public class Config {
    private static int listenPort = -1;
    private static String peerHostName = "-1";
    private static int peerPort = -1;
    private static boolean isMining = false;

    public static int getListenPort() {
        return listenPort;
    }
    public static String getPeerHostName() {
        return peerHostName;
    }
    public static int getPeerPort() {
        return peerPort;
    }
    public static boolean getIsMining() {
        return isMining;
    }

    public static void setListenPort(int lPort) {
        listenPort = lPort;
    }
    public static void setPeerHostName(String pHostName) {
        peerHostName = pHostName;
    }
    public static void setPeerPort(int pPort) {
        peerPort = pPort;
    }
    public static void setIsMining(boolean mining) {
        isMining = mining;
    }

    public static boolean isSet() {
        if (listenPort != -1 && !(peerHostName != "-1" ^ peerPort != -1)) {
            return true;
        } else {
            return false;
        }
    }
}
