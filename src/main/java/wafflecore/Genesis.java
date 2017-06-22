package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.model.*;
import wafflecore.Miner;
import wafflecore.util.BlockChainUtil;

import java.util.Arrays;
import java.util.ArrayList;

class Genesis {
    public static Block genesisBlock;
    public static final double INITIAL_DIFFICULTY = 2e-6;

    public void prepareGenesis(byte[] addr) {
        Transaction tx = Miner.createCoinbase(0, addr);
        ArrayList<byte[]> txIds = new ArrayList<byte[]>(Arrays.asList(tx.getId()));
        byte[] roottx = BlockChainUtil.rootHashTransactionIds(txIds);

        genesisBlock = new Block();
        genesisBlock.setPreviousHash(EMPTY_BYTES);
        genesisBlock.setId(EMPTY_BYTES);
        genesisBlock.setDifficulty(INITIAL_DIFFICULTY);
        genesisBlock.setTimestamp(System.currentTimeMillis());
        genesisBlock.setTransactionRootHash(roottx);
        genesisBlock.setTransactionIds(txIds);
        genesisBlock.setTransactions(new ArrayList<byte[]>(Arrays.asList(tx.getOriginal())));
        genesisBlock.setParsedTransactions(new ArrayList<Transaction>(Arrays.asList(tx)));

        Miner.mineGenesis(genesisBlock);

        System.out.println(genesisBlock.toJson());
    }

    public static Block getGenesisBlock() {
        return genesisBlock;
    }
}
