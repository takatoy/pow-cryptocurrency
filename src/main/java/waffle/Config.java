package waffle;

public class Config {
    private static int listenPort = -1;
    private static String peerHostName = "-1";
    private static int peerPort = -1;
    private static boolean isMining = false;
    private static boolean isGui = false;

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
    public static boolean getIsGui() {
        return isGui;
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
    public static void setIsGui(boolean gui) {
        isGui = gui;
    }

    public static boolean isSet() {
        if (listenPort != -1 && !(!"-1".equals(peerHostName) ^ peerPort != -1)) {
            return true;
        } else {
            return false;
        }
    }
}
