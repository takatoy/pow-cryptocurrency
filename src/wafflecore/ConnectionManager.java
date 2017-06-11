package wafflecore;

import wafflecore.Logger;
import wafflecore.WaffleCore;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.ArrayList;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ConnectionManager {
    private static Logger logger = Logger.getInstance();
    private boolean addToPeerList = false;
    private boolean listening = true;

    public Future<Void> listen(String host, int port) {
        return listen(new InetSocketAddress(host, port));
    }

    public Future<Void> listen(InetSocketAddress addr) {
        ExecutorService executor = WaffleCore.getExecutor();

        return executor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                try (ServerSocketChannel listener = ServerSocketChannel.open();) {
                    listener.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.TRUE);
                    listener.bind(addr);
                    logger.log("Server listening on port " + addr.getPort() + "...");
                    System.out.println("Server listening on port " + addr.getPort() + "...");

                    while (listening) {
                        final SocketChannel _channel = listener.accept();
                        logger.log("ACCEPTED " + _channel);

                        executor.submit(new Runnable() {
                            @Override
                            public void run() {
                                try (SocketChannel channel = _channel;) {
                                    // if (addToPeerList) {
                                    //     Peers.add(channel);
                                    // }
                                    response(channel);
                                    logger.log("CLOSED " + channel);
                                } catch (IOException e) {
                                    logger.log("Error listening server port.");
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
    }

    private void response(final SocketChannel _channel) {
        ByteBuffer buf = ByteBuffer.allocate(1000);
        Charset charset = Charset.forName("UTF-8");
        String remoteAddr = _channel.socket().getRemoteSocketAddress().toString();

        try {
            if (_channel.read(buf) < 0) {
                return;
            }
            String http = "";
            http += "HTTP/1.1 200 OK\n";
            http += "Content-Type: text/html\n";
            http += "\n";
            http += "<html><head><title>HEY</title></head><body><h1>Hello, World!</h1></body></html>";
            _channel.write(ByteBuffer.wrap(http.getBytes(charset)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
