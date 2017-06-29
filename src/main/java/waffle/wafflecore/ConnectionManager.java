package waffle.wafflecore;

import static waffle.wafflecore.constants.Constants.*;
import waffle.wafflecore.tool.Logger;
import waffle.wafflecore.WaffleCore;
import waffle.wafflecore.message.*;
import waffle.wafflecore.model.*;
import waffle.wafflecore.util.MessageUtil;

import org.apache.commons.lang3.ArrayUtils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.InetAddress;
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
    private ServerSocketChannel serverSocketChannel;

    private InetSocketAddress host;
    private HashMap<String, SocketChannel> peers = new HashMap<String, SocketChannel>();

    private ByteBuffer buf = ByteBuffer.allocate(MAX_BLOCK_SIZE + 1);

    private MessageHandler messageHandler;
    private BlockChainExecutor blockChainExecutor;

    public ConnectionManager(InetAddress hostAddr, int port) {
        this(new InetSocketAddress(hostAddr, port));
    }

    public ConnectionManager(InetSocketAddress host) {
        this.host = host;

        try {
            this.selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(host);
            serverSocketChannel.register(selector, serverSocketChannel.validOps());
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

                if (key.isConnectable()) {
                    handleConnect(key);
                } else if (key.isAcceptable()) {
                    handleAccept(key);
                } else if (key.isReadable()) {
                    handleRead(key);
                }
            }
        }
    }

    private void handleConnect(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();

        try {
            if (!socketChannel.finishConnect()) {
                return;
            }
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key) {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel;
        try {
            socketChannel = ssc.accept();
            socketChannel.configureBlocking(false);
            socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            logger.log("ACCEPTED: " + socketChannel);

            String peerAddr = socketChannel.getRemoteAddress().toString();
            peers.put(peerAddr, socketChannel);
            newPeer(peerAddr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRead(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        StringBuilder sb = new StringBuilder();
        String peerStr = "";

        try {
            buf.clear();
            int read = 0;

            while ((read = socketChannel.read(buf)) > 0) {
                buf.flip();
                byte[] bytes = new byte[buf.limit()];
                buf.get(bytes);
                sb.append(new String(bytes));
            }

            if (read == -1) {
                logger.log("Closing " + socketChannel);
                socketChannel.close();
                return;
            }

            peerStr = socketChannel.getRemoteAddress().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        sb.insert(0, '[');
        sb.append(']');
        replaceAll(sb, "}{", "},{");
        byte[] data = sb.toString().getBytes();

        Envelope[] envs = MessageUtil.deserializeArray(data);
        ArrayUtils.reverse(envs);

        for (Envelope env : envs) {
            messageHandler.handleMessage(env, peerStr);
        }
    }

    private static void replaceAll(StringBuilder sb, String from, String to) {
        int idx = sb.indexOf(from);
        while (idx != -1) {
            sb.replace(idx, idx + from.length(), to);
            idx += to.length();
            idx = sb.indexOf(from, idx);
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
                        if (key.isValid() && key.channel() instanceof SocketChannel) {
                            try {
                                SocketChannel socketChannel = (SocketChannel) key.channel();
                                socketChannel.write(buf);
                                buf.rewind();
                            } catch (IOException e) {
                                System.err.println("Connection error occured.");
                                key.channel().close();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    public void connectTo(String hostName, int port) {
        InetSocketAddress addr = new InetSocketAddress(hostName, port);
        SocketChannel socketChannel = null;

        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.socket().setReuseAddress(true);
            socketChannel.connect(addr);
            selector.wakeup();
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            String peerAddr = socketChannel.getRemoteAddress().toString();
            peers.put(peerAddr, socketChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void newPeer(String peerAddr) {
        Block genesis = Genesis.getGenesisBlock();

        Hello hello = new Hello(
            getPeers(),
            genesis.getId(),
            blockChainExecutor.getKnownBlockIds()
        );

        Envelope env = hello.packToEnvelope();
        asyncSend(MessageUtil.serialize(env), peerAddr);
    }

    public ArrayList<String> getPeers() {
        ArrayList<String> peerls = new ArrayList<String>();
        // WIP
        return peerls;
    }

    // setter
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }
    public void setBlockChainExecutor(BlockChainExecutor blockChainExecutor) {
        this.blockChainExecutor = blockChainExecutor;
    }
}
