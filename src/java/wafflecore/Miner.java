package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.tool.Logger;
import wafflecore.WaffleCore;
import wafflecore.BlockChainExecutor;
import wafflecore.model.*;
import wafflecore.util.BlockUtil;
import wafflecore.util.BlockChainUtil;
import java.security.SecureRandom;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;

public class Miner {
    private Logger logger = Logger.getInstance();
    private static boolean isMining = true;
    private static Future<Void> miner = null;
    private InventoryManager inventoryManager = null;
    private BlockChainExecutor blockChainExecutor = null;
    private byte[] recipientAddr;

    public static boolean mine(Block seed) {
        SecureRandom random = new SecureRandom();
        byte[] nonceSeed = new byte[Long.BYTES];
        random.nextBytes(nonceSeed);

        long nonce = ByteBuffer.wrap(nonceSeed).getLong(); // Byte array to Long
        while (isMining) {
            seed.setNonce(nonce++);
            seed.setTimestamp(System.currentTimeMillis());

            byte[] data = BlockUtil.serializeBlock(seed);
            byte[] blockId = BlockUtil.computeBlockId(data);

            if (BlockUtil.difficultyOf(blockId) > seed.getDifficulty()) {
                seed.setId(blockId);
                seed.setOriginal(data);
                return true;
            }
        }

        return false;
    }

    public void start() {
        isMining = true;
        ExecutorService executor = WaffleCore.getExecutor();

        miner = executor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                mineFromLastBlock();
                return null;
            }
        });
    }

    public void stop() {
        isMining = false;
        try {
            miner.get(); // Stop mining.
        } catch (Exception e) {}
    }

    public void notifyBlockMined() {
        if (!isMining) {
            return;
        }

        stop();
        start();
    }

    public void mineFromLastBlock() {
        int size = 350; // estimated block size
        ArrayList<Transaction> txs = new ArrayList<Transaction>();

        // Iteration over memory pool.
        for (Map.Entry<byte[], Transaction> txEntry : inventoryManager.memoryPool.entrySet()) {
            Transaction tx = txEntry.getValue();

            size += tx.getOriginal().length + 50;
            if (size > MAX_BLOCK_SIZE) {
                break;
            }
            txs.add(tx);
        }

        long blockTime = System.currentTimeMillis();
        long coinbase = BlockUtil.getCoinbaseAmount(blockChainExecutor.getLatestBlock().getHeight() + 1);
        ArrayList<TransactionOutput> txos = new ArrayList<TransactionOutput>();
        for (int i = 0; i < txs.size(); i++) {
            Transaction tx = txs.get(i);
            try {
                blockChainExecutor.runTransaction(tx, blockTime, 0, txos);
                TransactionExecInfo execinfo = tx.getExecInfo();
                coinbase += execinfo.getTransactionFee();
                txos.addAll(execinfo.getRedeemedOutputs());
            } catch (IllegalArgumentException e) {
                txs.remove(i);
            }
        }

        Transaction coinbaseTx = new Transaction();
        coinbaseTx.setTimestamp(blockTime);
        coinbaseTx.setInEntries(new ArrayList<InEntry>());

        OutEntry coinbaseOut = new OutEntry(recipientAddr, coinbase);
        coinbaseTx.setOutEntries(new ArrayList<OutEntry>(Arrays.asList(coinbaseOut)));

        blockChainExecutor.runTransaction(coinbaseTx, blockTime, coinbase, null);
        txs.add(0, coinbaseTx);

        ArrayList<byte[]> txIds = new ArrayList<byte[]>();
        ArrayList<byte[]> txOriginals = new ArrayList<byte[]>();
        for (Transaction tx : txs) {
            txIds.add(tx.getId());
            txOriginals.add(tx.getOriginal());
        }

        Block block = new Block();
        block.setPreviousHash(blockChainExecutor.getLatestBlock().getId());
        block.setDifficulty(BlockUtil.getNextDifficulty(
            BlockChainUtil.ancestors(blockChainExecutor.getLatestBlock(), blockChainExecutor.getBlocks())));
        block.setTransactionRootHash(BlockChainUtil.rootHashTransactionIds(txIds));

        // If mining succeed apply block.
        if (!mine(block)) {
            return;
        }

        block.setTransactionIds(txIds);
        block.setTransactions(txOriginals);
        block.setParsedTransactions(txs);

        logger.log("Block mined.");

        // @TODO broadcastasync(block);
    }

    // setter
    public void setInventoryManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }
    public void setBlockChainExecutor(BlockChainExecutor blockChainExecutor) {
        this.blockChainExecutor = blockChainExecutor;
    }
    public void setRecipientAddr(byte[] recipientAddr) {
        this.recipientAddr = recipientAddr;
    }
}
