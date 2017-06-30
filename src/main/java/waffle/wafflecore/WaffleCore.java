package waffle.wafflecore;

import static waffle.wafflecore.constants.Constants.*;
import waffle.Config;
import waffle.wafflecore.model.*;
import waffle.wafflecore.util.BlockChainUtil;
import waffle.wafflecore.util.ByteArrayWrapper;
import waffle.wafflecore.tool.Logger;
// import waffle.wafflecore.tool.Config;

import java.io.File;
import java.util.Scanner;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WaffleCore {
    private static ExecutorService service = null; // Thread Executor
    private static Logger logger = Logger.getInstance();
    private static boolean ready = false;

    public static void run() {
        int listenPort = -1;
        int peerPort = -1;
        String peerHostName = "";
        boolean isMining = false;

        if (Config.isSet()) {
            listenPort = Config.getListenPort();
            peerPort = Config.getPeerPort();
            peerHostName = Config.getPeerHostName();
            isMining = Config.getIsMining();
        } else {
            System.out.println("Invalid config.");
            System.exit(1);
        }

        // Initiate thread executor
        service = Executors.newCachedThreadPool();

        InetAddress hostAddr = null;
        try {
            hostAddr = InetAddress.getLocalHost();
        } catch (Exception e) {
            System.out.println("Error: Local host cannot be opened.");
            System.exit(1);
        }

        Genesis genesis = new Genesis();
        genesis.prepareGenesis();
        Block genesisBlock = Genesis.getGenesisBlock();

        Inventory inventory = new Inventory();
        BlockChainExecutor blockChainExecutor = new BlockChainExecutor();
        Miner miner = new Miner();
        MessageHandler messageHandler = new MessageHandler();
        ConnectionManager connectionManager = new ConnectionManager(hostAddr, listenPort);

        // Prepare BlockChainExecutor.
        blockChainExecutor.setMiner(miner);
        blockChainExecutor.setInventory(inventory);

        // Prepare Miner.
        miner.setBlockChainExecutor(blockChainExecutor);
        miner.setInventory(inventory);
        miner.setConnectionManager(connectionManager);
        miner.setMessageHandler(messageHandler);

        // Prepare MessageHandler.
        messageHandler.setInventory(inventory);
        messageHandler.setBlockChainExecutor(blockChainExecutor);
        messageHandler.setConnectionManager(connectionManager);

        // Prepare ConnectionManager.
        connectionManager.setMessageHandler(messageHandler);
        connectionManager.setBlockChainExecutor(blockChainExecutor);
        if (!"-1".equals(peerHostName) && peerPort != -1)
            connectionManager.connectTo(peerHostName, peerPort);

        connectionManager.start();

        // Process genesis block.
        inventory.blocks.put(genesisBlock.getId(), genesisBlock.getOriginal());
        blockChainExecutor.processBlock(genesisBlock.getOriginal(), genesisBlock.getPreviousHash());

        if (!"-1".equals(peerHostName) && peerPort != -1) {
            // Just in case wait for 5 seconds to start mining.
            logger.log("Preparing, please wait...");
            try {
                Thread.sleep(5000);
            } catch(Exception e) {
                e.printStackTrace();
            }

            while (!ready) {
                try {
                    Thread.sleep(100);
                } catch(Exception e) {}
            }
        }

        if (isMining) {
            miner.setRecipientAddr(BlockChainUtil.toAddress("Takato Yamazaki".getBytes()));
            miner.start();
        }

        Scanner scan = new Scanner(System.in);
        scan.next();
    }

    public static ExecutorService getExecutor() {
        return service;
    }

    public static void notifyReady() {
        ready = true;
    }
}
