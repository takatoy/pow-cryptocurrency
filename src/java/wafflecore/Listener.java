package wafflecore;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Listener implements Runnable {
    private static Logger logger = Logger.getInstance();
    InetSocketAddress addr = "";

    public Listener(String host, int port) {
        addr = new InetSocketAddress(host, port);
    }

    public Listener(InetSocketAddress addr) {
        this.addr = addr;
    }

    public void run() {
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
        } finally {
            worker.shutdown();
        }
    }

    private void response(SocketChannel channel) {
        ByteBuffer buf = ByteBuffer.allocate(1000);
        Charset charset = Charset.forName("UTF-8");
        String remoteAddr = channel.socket().getRemoteSocketAddress().toString();

        try {
            if (channel.read(buf) < 0) {
                return;
            }
            String http = "";
            http += "HTTP/1.1 200 OK\n";
            http += "Content-Type: text/html\n";
            http += "\n";
            http += "<html><head><title>HEY</title></head><body><h1>Hello, World!</h1></body></html>";
            channel.write(ByteBuffer.wrap(http.getBytes(charset)));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
