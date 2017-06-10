package wafflecore.connection;

import java.util.ArrayList;
import java.net.InetSocketAddress;

public class Peers {
    private static Peers peers = new Peers();
    private static ArrayList<ConnectionInfo> peerList;

    private Peers() {
    }

    public static Peers getInstance() {
        return peers;
    }

    public static ArrayList<ConnectionInfo> getList() {
        return peerList;
    }

    public static void add(String host, int port) {
        add(new InetSocketAddress(host, port));
    }

    public static void add(InetSocketAddress addr) {
        peerList.add(new ConnectionInfo(addr));
    }

    public static void remove(int id) {
        peerList.remove(id);
    }

    public static void clear() {
        peerList.clear();
    }
}
