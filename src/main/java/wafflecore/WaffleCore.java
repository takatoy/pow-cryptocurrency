package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.model.*;
import wafflecore.util.BlockChainUtil;
import wafflecore.util.ByteArrayWrapper;
// import wafflecore.tool.Config;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class WaffleCore {
    private static ExecutorService service = null; // Thread Executor

    public static void run() {
        // File dataDir = new File(DATA_DIR);
        // dataDir.mkdir();

        //////////////// DELETING IN FUTURE /////////////////
        Scanner scan = new Scanner(System.in);
        System.out.print("Listen Port Number: ");
        int port = scan.nextInt();
        System.out.print("Connect Host Name (null -> -1): ");
        String cHostName = scan.next();
        System.out.print("Connect Port Number (null -> -1): ");
        int cPort = scan.nextInt();
        //////////////// DELETING IN FUTURE /////////////////

        // Initiate thread executor
        service = Executors.newCachedThreadPool();

        Genesis genesis = new Genesis();
        genesis.prepareGenesis(BlockChainUtil.toAddress("Takato Yamazaki".getBytes()));
        Block genesisBlock = Genesis.getGenesisBlock();

        System.out.println("wow");
        Inventory inventory = new Inventory();
        BlockChainExecutor blockChainExecutor = new BlockChainExecutor();
        Miner miner = new Miner();
        MessageHandler messageHandler = new MessageHandler();
        ConnectionManager connectionManager = new ConnectionManager("127.0.0.1", port);

        // Prepare BlockChainExecutor.
        blockChainExecutor.setMiner(miner);
        blockChainExecutor.setInventory(inventory);

        // Prepare Miner.
        miner.setBlockChainExecutor(blockChainExecutor);
        miner.setInventory(inventory);
        miner.setConnectionManager(connectionManager);
        miner.setMessageHandler(messageHandler);

        System.out.println("hello");
        // Prepare MessageHandler.
        messageHandler.setInventory(inventory);
        messageHandler.setBlockChainExecutor(blockChainExecutor);
        messageHandler.setConnectionManager(connectionManager);

        // Prepare ConnectionManager.
        connectionManager.setMessageHandler(messageHandler);
        connectionManager.setBlockChainExecutor(blockChainExecutor);
        connectionManager.start();
        System.out.println("hello2");

        // Process genesis block.
        inventory.blocks.put(genesisBlock.getId(), genesisBlock.getOriginal());
        blockChainExecutor.processBlock(genesisBlock.getOriginal(), genesisBlock.getPreviousHash());

        scan.next();
        if (!"-1".equals(cHostName) && cPort != -1)
            connectionManager.asyncConnect(cHostName, cPort);

        boolean mine = true;
        if (mine) {
            miner.setRecipientAddr(BlockChainUtil.toAddress("Takato Yamazaki".getBytes()));
            miner.start();
        }
    }

    public static ExecutorService getExecutor() {
        return service;
    }
}
