package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.model.*;
import wafflecore.ConnectionManager;
import wafflecore.Genesis;
import wafflecore.util.BlockChainUtil;
import wafflecore.util.ByteArrayWrapper;

import java.io.File;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WaffleCore {
    private static ExecutorService service = null; // Thread Executor

    public static void run() {
        File dataDir = new File(DATA_DIR);
        dataDir.mkdir();

        // Initiate thread executor
        service = Executors.newCachedThreadPool();

        Genesis genesis = new Genesis();
        genesis.prepareGenesis(BlockChainUtil.toAddress("Takato Yamazaki".getBytes()));
        Block genesisBlock = Genesis.getGenesisBlock();

        Inventory inventory = new Inventory();
        BlockChainExecutor blockChainExecutor = new BlockChainExecutor();
        Miner miner = new Miner();
        MessageHandler messageHandler = new MessageHandler();
        ConnectionManager connectionManager = new ConnectionManager("localhost", 9001);

        blockChainExecutor.setMiner(miner);
        blockChainExecutor.setInventory(inventory);
        miner.setBlockChainExecutor(blockChainExecutor);
        miner.setInventory(inventory);
        connectionManager.setMessageHandler(messageHandler);

        inventory.blocks.put(genesisBlock.getId(), genesisBlock.getOriginal());
        blockChainExecutor.processBlock(genesisBlock.getOriginal(), genesisBlock.getPreviousHash());

        miner.setRecipientAddr(BlockChainUtil.toAddress("Takato Yamazaki".getBytes()));
        miner.start();
    }

    public static ExecutorService getExecutor() {
        return service;
    }
}
