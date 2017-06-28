package wafflecore;

import wafflecore.tool.Logger;
import wafflecore.WaffleCore;
import wafflecore.message.*;
import wafflecore.util.MessageUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.channels.SocketChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ConnectionManager {
    private static Logger logger = Logger.getInstance();
    private boolean listening = true;
    private Selector selector;
    private ByteBuffer buf = ByteBuffer.allocate(256);
    private HashMap<String, SocketChannel> peers = new HashMap<String, SocketChannel>();

    private MessageHandler messageHandler;

    public ConnectionManager(String hostName, int port) {
        this(new InetSocketAddress(hostName, port));
    }

    public ConnectionManager(InetSocketAddress host) {
        try {
            this.selector = Selector.open();
            ServerSocketChannel sChannel = ServerSocketChannel.open();
            sChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            sChannel.configureBlocking(false);
            sChannel.socket().bind(host);
            sChannel.register(selector, sChannel.validOps());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        ExecutorService executor = WaffleCore.getExecutor();

        executor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                listen();
                return null;
            }
        });
    }

    public void listen() {
        while (listening) {
            try {
                selector.select();
            } catch (IOException e) {
                return;
            }

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {
                SelectionKey key = (SelectionKey) it.next();
                it.remove();

                if (!key.isValid()) {
                    continue;
                }

                if (key.isAcceptable()) {
                    handleAccept(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    private void handleAccept(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel;
        try {
            socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            logger.log("ACCEPTED: " + socketChannel);

            peers.put(socketChannel.getRemoteAddress().toString(), socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRead(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        byte[] data = null;
        String peerStr = "";

        try {
            buf.clear();
            int read = 0;
            while ((read = socketChannel.read(buf)) > 0) {
                buf.flip();
                byte[] bytes = new byte[buf.limit()];
                buf.get(bytes);
            }
            buf.get(data);

            peerStr = socketChannel.getRemoteAddress().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (data != null) {
            Envelope env = MessageUtil.deserialize(data);
            messageHandler.handleMessage(env, peerStr);
        }
    }

    /**
     *  Send message to all peers.
     */
    public void asyncBroadcast(byte[] msg) {
        ExecutorService executor = WaffleCore.getExecutor();

        executor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                try {
                    ByteBuffer buf = ByteBuffer.wrap(msg);
                    for (SelectionKey key : selector.keys()) {
                        if (key.isValid() && key.isWritable() && key.channel() instanceof SocketChannel) {
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            socketChannel.write(buf);
                            buf.rewind();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public void asyncSend(byte[] msg, String addr) {
        ExecutorService executor = WaffleCore.getExecutor();

        executor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                try {
                    ByteBuffer buf = ByteBuffer.wrap(msg);
                    SocketChannel socketChannel = peers.get(addr);
                    socketChannel.write(buf);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        });
    }

    // setter
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    // Will delete, just used for test
    // private void response(SocketChannel channel) {
    //     ByteBuffer buf = ByteBuffer.allocate(1000);
    //     Charset charset = Charset.forName("UTF-8");
    //     String remoteAddr = channel.socket().getRemoteSocketAddress().toString();

    //     try {
    //         if (channel.read(buf) < 0) {
    //             return;
    //         }
    //         String http = "";
    //         http += "HTTP/1.1 200 OK\n";
    //         http += "Content-Type: text/html\n";
    //         http += "\n";
    //         http += "<html><head><title>HEY</title></head><body><h1>Hello, World!</h1></body></html>";
    //         channel.write(ByteBuffer.wrap(http.getBytes(charset)));
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return;
    //     }
    // }
}
