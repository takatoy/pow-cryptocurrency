package wafflecore;

import wafflecore.model.*;
import wafflecore.util.BlockChainUtil;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collections;

class Executor {
    Logger logger = Logger.getInstance();

    // key: block id / value: Block
    public Hashtable<byte[], Block> blocks = new Hashtable<byte[], Block>();
    // key: ancestor block id / value: floating block ids
    public Hashtable<byte[], ArrayList<byte[]>> floatingBlocks = new Hashtable<byte[], ArrayList<byte[]>>();
    public Hashtable<TransactionOutput, TransactionOutput> utxos = new Hashtable<TransactionOutput, TransactionOutput>();

    public Block latest;

    public Executor() {
        latest = Block.getGenesisBlock();
        blocks.put(latest.getId(), latest);
    }

    synchronized public void processBlock(byte[] data, byte[] prevId) {
        Block prevBlock = blocks.get(prevId);

        if (prevBlock == null) {
            // When previous block was not found, the block is put to floating block.
            ArrrayList<byte[]> flBlocks = floatingBlocks.get(prevId);
            if (flBlocks == null) {
                flBlocks = new ArrayList<byte[]>();
            }
            flBlocks.put(prevId, flBlocks);
        }

        // Mark block as connected.
        Block blk = BlockChainUtil.deserializeBlock(data);
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
            Block block = iter.next();
            if (fork.getId().equals(block.getId())) {
                break;
            }
            revertingChain.addLast(block);
            revert(block);
        }
        // Then apply chain of received block.
        ArrayList<Block> applyingChain = new ArrayList<Block>();
        ArrayList<Block> ancestorsFromBlk = BlockChainUtil.ancestors(blk, blocks);
        for (Block block : ancestorsFromBlk) {
            if (fork.getId().equals(block.getId())) {
                break;
            }
            applyingChain.addFirst(block); // Add in opposite order.
        }

        // Apply.
        int failId = -1;
        for (int i = 0; i < applyingChain.size(); i++) {
            Block applyBlock = applyingChain.get(i);
            try {
                run(applyBlock);
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

    public void run(Block block) {
        // WIP
    }

    public void apply(Block block) {
        String idStr = new String(block.getId());
        idStr = idStr.substring(0, 7);
        logger.log("Applying block " + block.height() + ":" + idStr);
    }

    public void checkFloatingBlocks(byte[] blockId) {
        // WIP
    }

    public void purgeBlock(byte[] id) {

    }

    public void revert(Block block) {
        // WIP
    }
}
