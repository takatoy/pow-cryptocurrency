package wafflecore;

import wafflecore.model.*;
import wafflecore.util.BlockChainUtil;
import wafflecore.util.BlockUtil;
import wafflecore.util.TransactionUtil;
import wafflecore.util.EccService;
import wafflecore.tool.SystemUtil;
import wafflecore.tool.Logger;
import wafflecore.Genesis;
import wafflecore.util.ByteArrayWrapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

class BlockChainExecutor {
    private Logger logger = Logger.getInstance();
    private InventoryManager inventoryManager;
    private Miner miner;

    // key: block id / value: Block
    private ConcurrentHashMap<ByteArrayWrapper, Block> blocks = new ConcurrentHashMap<ByteArrayWrapper, Block>();
    // key: ancestor block id / value: floating block ids
    private ConcurrentHashMap<ByteArrayWrapper, ArrayList<ByteArrayWrapper>> floatingBlocks = new ConcurrentHashMap<ByteArrayWrapper, ArrayList<ByteArrayWrapper>>();
    private ConcurrentHashMap<ByteArrayWrapper, TransactionOutput> utxos = new ConcurrentHashMap<ByteArrayWrapper, TransactionOutput>();

    private Block latest;

    public BlockChainExecutor() {
        latest = Genesis.getGenesisBlock();
        blocks.put(latest.getId(), latest);
    }

    synchronized public void processBlock(byte[] data, ByteArrayWrapper prevId) {
        Block prevBlock = blocks.get(prevId);

        if (prevBlock == null) {
            // When previous block was not found, the block is put to floating block.
            ArrayList<ByteArrayWrapper> flBlocks = floatingBlocks.get(prevId);
            if (flBlocks == null) {
                flBlocks = new ArrayList<ByteArrayWrapper>();
                floatingBlocks.put(prevId, new ArrayList<ByteArrayWrapper>());
            }
            flBlocks.add(prevId);
            return;
        }

        // Mark block as connected.
        Block blk = BlockUtil.deserializeBlock(data);

        blk.setHeight(prevBlock.getHeight() + 1);
        blk.setTotalDifficulty(blk.getDifficulty() + prevBlock.getTotalDifficulty());
        blocks.put(blk.getId(), blk);

        // If block's total difficulty did not surpass latest's,
        // process later.
        if (latest.getTotalDifficulty() >= blk.getTotalDifficulty()) {
            checkFloatingBlocks(blk.getId());
            return;
        }

        Block fork = BlockChainUtil.lowestCommonAncestor(latest, blk, blocks);

        // Once revert chain to fork.
        ArrayList<Block> revertingChain = new ArrayList<Block>();
        ArrayList<Block> ancestorsFromLatest = BlockChainUtil.ancestors(latest, blocks);
        for (Block block : ancestorsFromLatest) {
            if (fork.getId().equals(block.getId())) {
                break;
            }
            revertingChain.add(block);
            revert(block);
        }
        // Then apply chain of received block.
        ArrayList<Block> applyingChain = new ArrayList<Block>();
        ArrayList<Block> ancestorsFromBlk = BlockChainUtil.ancestors(blk, blocks);
        for (Block block : ancestorsFromBlk) {
            if (fork.getId().equals(block.getId())) {
                break;
            }
            applyingChain.add(block);
        }
        Collections.reverse(applyingChain);

        // Apply.
        int failId = -1;
        for (int i = 0; i < applyingChain.size(); i++) {
            Block applyBlock = applyingChain.get(i);
            try {
                runBlock(applyBlock);
            } catch (Exception e) {
                // Failed to apply.
                purgeBlock(applyBlock.getId());
                failId = i;
                break;
            }
            apply(applyBlock);
        }

        if (failId != -1) {
            // Revert to original blockchain.
            for (int i = failId; i >= 0; i--) {
                revert(applyingChain.get(i));
            }
            Collections.reverse(revertingChain);
            for (Block block : revertingChain) {
                apply(block);
            }
            return;
        }

        checkFloatingBlocks(blk.getId());
    }

    // Validation and adding parameters to block.
    public void runBlock(Block block) {
        String idStr = block.getId().toString().substring(0, 7);
        logger.log("Run block:" + idStr);

        if (block.getParsedTransactions() != null) {
            return;
        }

        byte[] rootTxHash = BlockChainUtil.rootHashTransactionIds(block.getTransactionIds());
        ArrayList<Block> prevBlocks = BlockChainUtil.ancestors(block, blocks);
        prevBlocks.remove(prevBlocks.size() - 1); // Remove the genesis block.
        double difficulty = BlockUtil.getNextDifficulty(prevBlocks);

        // Throw exception if block is invalid.
        if (block.getTimestamp() > System.currentTimeMillis() ||
            block.getTimestamp() < latest.getTimestamp() ||
            block.getTransactions().size() == 0 ||
            block.getTransactions().size() != block.getTransactionIds().size() ||
            !Arrays.equals(rootTxHash, block.getTransactionRootHash()) ||
            !latest.getId().equals(block.getPreviousHash()) ||
            block.getDifficulty() >= difficulty * (1 + 1e-15) ||
            block.getDifficulty() <= difficulty * (1 - 1e-15) ||
            block.getDifficulty() > BlockUtil.difficultyOf(block.getId()))
        {
            throw new IllegalArgumentException();
        }

        // Check transactions. Deserialize bytes.
        ArrayList<byte[]> txs = block.getTransactions();
        ArrayList<ByteArrayWrapper> txIds = block.getTransactionIds();
        ArrayList<Transaction> parsedTxs = new ArrayList<Transaction>();
        for (int i = 0; i < txs.size(); i++) {
            Transaction tx = TransactionUtil.deserializeTransaction(txs.get(i));
            if (!tx.getId().equals(txIds.get(i))) {
                throw new IllegalArgumentException();
            }
            parsedTxs.add(tx);
        }

        long coinbase = BlockUtil.getCoinbaseAmount(latest.getHeight() + 1);
        ArrayList<TransactionOutput> spentTxos = new ArrayList<TransactionOutput>();
        for (int i = 1; i < parsedTxs.size(); i++) {
            runTransaction(parsedTxs.get(i), block.getTimestamp(), 0, spentTxos);
            TransactionExecInfo execInfo = parsedTxs.get(i).getExecInfo();

            // Collect all transaction fees and add it to coinbase.
            coinbase += execInfo.getTransactionFee();
            spentTxos.addAll(execInfo.getRedeemedOutputs());
        }

        runTransaction(parsedTxs.get(0), block.getTimestamp(), coinbase, null);
        block.setHeight(latest.getHeight() + 1);
        block.setParsedTransactions(parsedTxs);
        block.setTotalDifficulty(latest.getTotalDifficulty() + block.getDifficulty());
    }

    // Validation and adding parameters to transactions.
    public void runTransaction(Transaction tx, long blockTime, long coinbase, ArrayList<TransactionOutput> spentTxos) {
        String idStr = tx.getId().toString().substring(0, 7);
        logger.log("Run Transaction:" + idStr);

        if (tx.getTimestamp() > blockTime ||
            !(coinbase == 0 ^ tx.getInEntries().size() == 0)) {
            throw new IllegalArgumentException();
        }

        // Validity check for in-entries.
        long inSum = coinbase;
        ArrayList<TransactionOutput> redeemed = new ArrayList<TransactionOutput>();
        byte[] signHash = TransactionUtil.getTransactionSignHash(tx.getOriginal());
        ArrayList<InEntry> inEntries = tx.getInEntries();
        for (InEntry in : inEntries) {
            // Verify signature.
            boolean isVerified = EccService.verify(signHash, in.getSignature(), in.getPublicKey());
            TransactionOutput txo = new TransactionOutput();
            txo.setTransactionId(in.getTransactionId());
            txo.setOutIndex(in.getOutEntryIndex());

            // Check if transaction output is unspent.
            boolean isUnspent = true;
            if (spentTxos != null) {
                for (TransactionOutput spent : spentTxos) {
                    if (spent.getTransactionId().equals(txo.getTransactionId())) {
                        isUnspent = false;
                    }
                }
            }

            txo = utxos.get(txo.getTransactionId());
            boolean isRedeemable = false;
            if (txo == null) {
                isUnspent = false;
            } else {
                // Check recipient address.
                byte[] addr = BlockChainUtil.toAddress(in.getPublicKey());
                isRedeemable = txo.getRecipient().equals(addr);
            }

            inSum += txo.getAmount();
            if (!isVerified || !isUnspent || !isRedeemable) {
                throw new IllegalArgumentException();
            }

            redeemed.add(txo);
        }

        long outSum = 0;
        short outIndex = 0;
        ArrayList<OutEntry> outEntries = tx.getOutEntries();
        ArrayList<TransactionOutput> generated = new ArrayList<TransactionOutput>();
        for (OutEntry out : outEntries) {
            if (out.getRecipientHash() == null || out.getRecipientHash().length == 0 || out.getAmount() <= 0) {
                throw new IllegalArgumentException();
            }

            outSum += out.getAmount();

            generated.add(new TransactionOutput(tx.getId(), outIndex, out.getRecipientHash(), out.getAmount()));
            outIndex++;
        }

        if (outSum > inSum) {
            throw new IllegalArgumentException();
        }

        tx.setExecInfo(new TransactionExecInfo(coinbase != 0, redeemed, generated, inSum - outSum));
    }

    public void apply(Block block) {
        String idStr = block.getId().toString().substring(0, 7);
        logger.log("Applying block " + block.getHeight() + ":" + idStr);

        ArrayList<Transaction> txs = block.getParsedTransactions();
        ArrayList<ByteArrayWrapper> txIds = new ArrayList<ByteArrayWrapper>();
        for (Transaction tx : txs) {
            if (tx.getExecInfo() != null && !tx.getExecInfo().getCoinbase()) {
                txIds.add(tx.getId());
            }
        }

        synchronized (inventoryManager.memoryPool) {
            for (ByteArrayWrapper txId : txIds) {
                inventoryManager.memoryPool.remove(txId);
            }
        }

        for (Transaction tx : txs) {
            tx.getExecInfo().getRedeemedOutputs().forEach(x -> utxos.remove(x.getTransactionId()));
            tx.getExecInfo().getGeneratedOutputs().forEach(x -> utxos.put(x.getTransactionId(), x));
        }

        latest = block;

        miner.notifyBlockApplied();
    }

    public void revert(Block block) {
        String idStr = block.getId().toString().substring(0, 7);
        logger.log("Revert block " + block.getHeight() + ":" + idStr);

        ArrayList<Transaction> txs = block.getParsedTransactions();
        ArrayList<Transaction> txls = new ArrayList<Transaction>();
        for (Transaction tx : txs) {
            if (!tx.getExecInfo().getCoinbase()) {
                txls.add(tx);
            }
        }

        synchronized (inventoryManager.memoryPool) {
            for (Transaction tx : txls) {
                inventoryManager.memoryPool.put(tx.getId(), tx);
            }
        }

        for (Transaction tx : txs) {
            tx.getExecInfo().getRedeemedOutputs().forEach(x -> utxos.put(x.getTransactionId(), x));
            tx.getExecInfo().getGeneratedOutputs().forEach(x -> utxos.remove(x.getTransactionId()));
        }

        latest = blocks.get(block.getPreviousHash());
        // BlockExecuted()
    }

    public void checkFloatingBlocks(ByteArrayWrapper waitingBlockId) {
        ArrayList<ByteArrayWrapper> pendingBlocks = floatingBlocks.get(waitingBlockId);
        if (pendingBlocks == null) {
            return;
        }

        for (ByteArrayWrapper floatingBlockId : pendingBlocks) {
            byte[] blockData;

            blockData = inventoryManager.blocks.get(floatingBlockId);

            if (blockData == null) {
                continue;
            }

            processBlock(blockData, waitingBlockId);
        }
    }

    public void purgeBlock(ByteArrayWrapper id) {
        Block block = blocks.get(id);
        if (block != null) {
            if (block.getParsedTransactions() == null || block.getParsedTransactions().size() == 0) {
                return;
            }
            blocks.remove(id);
        }

        ArrayList<ByteArrayWrapper> blockIds = floatingBlocks.get(id);
        if (blocks != null) {
            floatingBlocks.remove(id);
            blockIds.forEach(x -> purgeBlock(x));
        }

        inventoryManager.blocks.remove(id);
    }

    // getter
    public ConcurrentHashMap<ByteArrayWrapper, Block> getBlocks() {
        return blocks;
    }
    public ConcurrentHashMap<ByteArrayWrapper, TransactionOutput> getUtxos() {
        return utxos;
    }
    public Block getLatestBlock() {
        return latest;
    }
    // setter
    public void setInventoryManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }
    public void setMiner(Miner miner) {
        this.miner = miner;
    }
}
