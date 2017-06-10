package wafflecore.connection;

import java.net.InetSocketAddress;

public class ConnectionInfo {
    private String host = "";
    private int port = 0;

    public ConnectionInfo(InetSocketAddress addr) {
        // this.host = host;
        // this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }
}
