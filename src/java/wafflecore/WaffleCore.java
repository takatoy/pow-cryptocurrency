package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.model.*;
import wafflecore.ConnectionManager;
import wafflecore.Genesis;
import wafflecore.util.BlockChainUtil;

import java.io.File;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;

public class WaffleCore {
    private static ExecutorService service = null; // Thread Executor

    public static void main(String[] args) throws Exception {
        File dataDir = new File(DATA_DIR);
        dataDir.mkdir();

        // Initiate thread executor
        service = Executors.newCachedThreadPool();

        // ConnectionManager connectionManager = new ConnectionManager("localhost", 9001);
        // connectionManager.listen(); // listen

        // System.out.println("Program still going.");
        // try {
        //     Thread.sleep(5000);
        // } catch (Exception e) {}

        // connectionManager.asyncBroadcast("Hello?");

        InventoryManager inventoryManager = new InventoryManager();
        BlockChainExecutor blockChainExecutor = new BlockChainExecutor();
        Miner miner = new Miner();

        // blockChainExecutor.setMiner(miner);
        // blockChainExecutor.setInventoryManager(inventoryManager);
        // miner.setBlockChainExecutor(blockChainExecutor);
        // miner.setInventoryManager(inventoryManager);

        // Block genesis = Genesis.getGenesisBlock();
        // inventoryManager.blocks.put(genesis.getId(), genesis.getOriginal());
        // blockChainExecutor.processBlock(genesis.getOriginal(), genesis.getPreviousHash());

        // miner.setRecipientAddr(BlockChainUtil.toAddress("Takato Yamazaki".getBytes()));
        // miner.start();

        miner.mine()
    }

    public static ExecutorService getExecutor() {
        return service;
    }
}
