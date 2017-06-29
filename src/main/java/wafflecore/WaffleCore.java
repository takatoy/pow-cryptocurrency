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
        //////////////// DELETING IN FUTURE /////////////////
        Scanner scan = new Scanner(System.in);
        System.out.print("Listen Port Number: ");
        int port = scan.nextInt();
        System.out.print("Connect Host Name (null -> -1): ");
        String cHostName = scan.next();
        System.out.print("Connect Port Number (null -> -1): ");
        int cPort = scan.nextInt();
        System.out.print("Mine?: ");
        boolean mine = scan.nextBoolean();
        //////////////// DELETING IN FUTURE /////////////////

        // Initiate thread executor
        service = Executors.newCachedThreadPool();

        Genesis genesis = new Genesis();
        genesis.prepareGenesis();
        Block genesisBlock = Genesis.getGenesisBlock();

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

        // Prepare MessageHandler.
        messageHandler.setInventory(inventory);
        messageHandler.setBlockChainExecutor(blockChainExecutor);
        messageHandler.setConnectionManager(connectionManager);

        // Prepare ConnectionManager.
        connectionManager.setMessageHandler(messageHandler);
        connectionManager.setBlockChainExecutor(blockChainExecutor);
        if (!"-1".equals(cHostName) && cPort != -1)
            connectionManager.connectTo(cHostName, cPort);

        connectionManager.start();

        // Process genesis block.
        inventory.blocks.put(genesisBlock.getId(), genesisBlock.getOriginal());
        blockChainExecutor.processBlock(genesisBlock.getOriginal(), genesisBlock.getPreviousHash());

        // Just in case wait for 5 seconds to start mining.
        try {
            Thread.sleep(5000);
        } catch(Exception e) {
            e.printStackTrace();
        }

        // boolean mine = true;
        if (mine) {
            miner.setRecipientAddr(BlockChainUtil.toAddress("Takato Yamazaki".getBytes()));
            miner.start();
        }
        scan.next();
    }

    public static ExecutorService getExecutor() {
        return service;
    }
}
