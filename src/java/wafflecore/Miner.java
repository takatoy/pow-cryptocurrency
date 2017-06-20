package wafflecore;

import wafflecore.tool.Logger;
import wafflecore.WaffleCore;
import wafflecore.model.*;
import wafflecore.util.BlockUtil;
import java.security.SecureRandom;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Miner {
    private Logger logger = Logger.getInstance();
    private static boolean isMining = true;
    private static Future<boolean> miner = null;
    private InventoryManager inventoryManager = null;
    private Executor executor = null;
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
        ExecutorService executor = Wafflecore.getExecutor();

        miner = executor.submit(new Callable<boolean>() {
            @Override
            public boolean call() {
                return mineFromLastBlock();
            }
        });
    }

    public void stop() {
        isMining = false;
        miner.get(); // Stop mining.
    }

    public void notify() {
        if (!isMining) {
            return;
        }

        stop();
        start();
    }

    public void mineFromLastBlock() {
        int size = 350; // estimated block size
        ArrayList<Transaction> txs = new ArrayList<Transaction>();

        for (Transaction tx : inventoryManager.memoryPool) {
            size += txs.getOriginal().length + 50;
            if (size > MAX_BLOCK_SIZE) {
                break;
            }
            txs.add();
        }

        long blockTime = System.currentTimeMillis();
        long coinbase = BlockUtil.getCoinbaseAmount();
        ArrayList<TransactionOutput> txos = new ArrayList<TransactionOutput>();
        for (int i = 0; i < txs.size(); i++) {
            try {
                executor.runTransaction(tx, blockTime, 0, txos);
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

        OutEntry coinbaseOut = new OutEntry();
        coinbaseOut.setRecipientHash(recipientAddr);
        coinbaseOut.setAmount(coinbase);

        coinbaseTx.setOutEntries(new ArrayList<OutEntry>(Arrays.asList(coinbaseOut)));

        executor.runTransaction(coinbaseTx, blockTime, coinbase, null);
        txs.add(0, coinbaseTx);

        ArrayList<byte[]> txIds = new ArrayList<byte[]>();
        ArrayList<byte[]> txOriginals = new ArrayList<byte[]>();
        for (Transaction tx : txs) {
            txIds.add(tx.getId());
            txOriginals.add(tx.getOriginal());
        }

        Block block = new Block();
        block.setPreviousHash(executor.getLatestBlock.getId());
        block.setDifficulty(BlockUtil.getNextDifficulty(
            BlockChainUtil.ancestors(executor.getLatestBlock(), executor.getBlocks())));
        block.setTransactionRootHash(BlockChainUtil.rootHashTransactionIds(txIds));

        // If mining succeed apply block.
        if (!Mine(block)) {
            return;
        }

        block.setTransactionIds(txIds);
        block.setTransactions(txOriginals);
        block.setParsedTransactions(txs);

        logger.log("Block mined.");

        // @TODO broadcastasync(block);
    }

    // setter
    public void setInventoryManager(InventoryManger inventoryManager) {
        this.inventoryManager = inventoryManager;
    }
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }
    public void setRecipientAddr(byte[] recipientAddr) {
        this.recipientAddr = recipientAddr;
    }
}
