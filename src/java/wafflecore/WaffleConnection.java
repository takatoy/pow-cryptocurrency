package wafflecore;

import wafflecore.WaffleLogger;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class WaffleConnection {
    // private static WaffleConnection waffleConnection = new WaffleConnection();
    // private WaffleConnection() {
    // }

    // public WaffleConnection getInstance() {
    //     return waffleConnection;
    // }

    private static WaffleLogger logger = WaffleLogger.getInstance();

    public void listen(InetSocketAddress addr) {
        ExecutorService worker = Executors.newCachedThreadPool();

        try (ServerSocketChannel listener = ServerSocketChannel.open();) {
            listener.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.TRUE);
            listener.bind(addr);
            logger.log("Server listening on port " + addr.getPort() + "...");
            System.out.println("Server listening on port " + addr.getPort() + "...");

            while (true) {
                final SocketChannel _channel = listener.accept();
                logger.log("ACCEPTED " + _channel);

                worker.submit(new Runnable() {
                    @Override
                    public void run() {
                        try (SocketChannel channel = _channel;) {
                            // do something
                            echo(channel);
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
        } finally {
            worker.shutdown();
        }
    }

    private void echo(SocketChannel channel) {
        ByteBuffer buf = ByteBuffer.allocate(1000);
        Charset charset = Charset.forName("UTF-8");
        String remoteAddr = channel.socket().getRemoteSocketAddress().toString();

        try {
            if (channel.read(buf) < 0) {
                return;
            }
            buf.flip();
            String input = charset.decode(buf).toString();
            logger.log(remoteAddr + ":" + input);
            buf.flip();
            channel.write(buf);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
