package wafflecore.connection;

import wafflecore.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class Listener extends Thread {
    private static Logger logger = Logger.getInstance();
    private InetSocketAddress addr = null;
    private boolean running = true;

    public Listener(String host, int port) {
        addr = new InetSocketAddress(host, port);
    }

    public Listener(InetAddress addr, int port) {
        this.addr = new InetSocketAddress(addr, port);
    }

    public Listener(InetSocketAddress addr) {
        this.addr = addr;
    }

    @Override
    public void run() {
        ExecutorService worker = Executors.newCachedThreadPool();

        try (ServerSocketChannel listener = ServerSocketChannel.open();) {
            listener.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.TRUE);
            listener.bind(addr);
            logger.log("Server listening on port " + addr.getPort() + "...");
            System.out.println("Server listening on port " + addr.getPort() + "...");

            while (running) {
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

    public void stopRunning() {
        running = false;
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
