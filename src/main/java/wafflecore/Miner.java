package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.tool.Logger;
import wafflecore.tool.SystemUtil;
import wafflecore.WaffleCore;
import wafflecore.BlockChainExecutor;
import wafflecore.model.*;
import wafflecore.util.BlockUtil;
import wafflecore.util.BlockChainUtil;
import wafflecore.util.TransactionUtil;
import wafflecore.util.ByteArrayWrapper;

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
    public static boolean isMining = false;
    private static Future<Void> miner = null;
    private Inventory inventory = null;
    private BlockChainExecutor blockChainExecutor = null;
    private byte[] recipientAddr;

    public static boolean mineGenesis(Block genesis) {
        isMining = true;
        boolean ret = mine(genesis);
        isMining = false;

        return ret;
    }

    public static boolean mine(Block seed) {
        SecureRandom random = new SecureRandom();
        byte[] nonceSeed = new byte[Long.BYTES];
        random.nextBytes(nonceSeed);

        long nonce = ByteBuffer.wrap(nonceSeed).getLong(); // Byte array to Long
        while (isMining) {
            seed.setNonce(nonce++);
            seed.setTimestamp(System.currentTimeMillis());
            // System.out.println(nonce);

            byte[] data = BlockUtil.serialize(seed);
            ByteArrayWrapper blockId = BlockUtil.computeBlockId(data);

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

    public void notifyBlockApplied() {
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
        for (Map.Entry<ByteArrayWrapper, Transaction> txEntry : inventory.memoryPool.entrySet()) {
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
            System.out.println(tx);
            try {
                blockChainExecutor.runTransaction(tx, blockTime, 0, txos);
                TransactionExecInfo execinfo = tx.getExecInfo();
                coinbase += execinfo.getTransactionFee();
                txos.addAll(execinfo.getRedeemedOutputs());
            } catch (Exception e) {
                txs.remove(i);
            }
        }

        Transaction coinbaseTx = new Transaction();
        coinbaseTx.setTimestamp(blockTime);
        coinbaseTx.setInEntries(new ArrayList<InEntry>());

        OutEntry coinbaseOut = new OutEntry(recipientAddr, coinbase);
        coinbaseTx.setOutEntries(new ArrayList<OutEntry>(Arrays.asList(coinbaseOut)));

        coinbaseTx = TransactionUtil.deserialize(TransactionUtil.serialize(coinbaseTx));

        try {
            blockChainExecutor.runTransaction(coinbaseTx, blockTime, coinbase, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        txs.add(0, coinbaseTx);

        ArrayList<ByteArrayWrapper> txIds = new ArrayList<ByteArrayWrapper>();
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

        String idStr = block.getId().toString().substring(0, 7);
        logger.log("Block mined:" + idStr);
        System.out.println(block.toJson());

        // @TODO broadcastasync(block);

        byte[] serialized = BlockUtil.serialize(block);
        blockChainExecutor.processBlock(serialized, block.getPreviousHash());
    }

    public static Transaction createCoinbase(int height, byte[] recipient) {
        Transaction tx = new Transaction();
        OutEntry coinbaseOut = new OutEntry(recipient, BlockUtil.getCoinbaseAmount(0));

        tx.setTimestamp(System.currentTimeMillis());
        tx.setInEntries(new ArrayList<InEntry>());
        tx.setOutEntries(new ArrayList<OutEntry>(Arrays.asList(coinbaseOut)));

        byte[] serialized = TransactionUtil.serialize(tx);
        tx.setOriginal(serialized);
        tx.setId(TransactionUtil.computeTransactionId(serialized));

        return tx;
    }

    // setter
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    public void setBlockChainExecutor(BlockChainExecutor blockChainExecutor) {
        this.blockChainExecutor = blockChainExecutor;
    }
    public void setRecipientAddr(byte[] recipientAddr) {
        this.recipientAddr = recipientAddr;
    }
}
