package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.model.*;
import wafflecore.Miner;
import wafflecore.util.TransactionUtil;
import wafflecore.util.BlockChainUtil;
import wafflecore.util.BlockUtil;
import wafflecore.tool.Logger;
import wafflecore.util.ByteArrayWrapper;

import java.util.Base64;
import java.util.Arrays;
import java.util.ArrayList;

class Genesis {
    public static Block genesisBlock;
    public static final double INITIAL_DIFFICULTY = 2e-6;
    public static Logger logger = Logger.getInstance();

    public void prepareGenesis() {
        // Transaction tx = Miner.createCoinbase(0, addr);
        // ArrayList<ByteArrayWrapper> txIds = new ArrayList<ByteArrayWrapper>(Arrays.asList(tx.getId()));

        Transaction tx = new Transaction();
        OutEntry coinbaseOut = new OutEntry(Base64.getDecoder().decode("gWfb7COjt35xcczSH2PiR6A9yA1m92qc/bUoZqpAL6w="), BlockUtil.getCoinbaseAmount(0));

        tx.setTimestamp(1498699423171l);
        tx.setInEntries(new ArrayList<InEntry>());
        tx.setOutEntries(new ArrayList<OutEntry>(Arrays.asList(coinbaseOut)));

        byte[] serialized = TransactionUtil.serialize(tx);
        tx.setOriginal(serialized);
        tx.setId(TransactionUtil.computeTransactionId(serialized));

        ArrayList<ByteArrayWrapper> txIds = new ArrayList<ByteArrayWrapper>(Arrays.asList(tx.getId()));
        byte[] roottx = BlockChainUtil.rootHashTransactionIds(txIds);

        genesisBlock = new Block();
        genesisBlock.setPreviousHash(ByteArrayWrapper.copyOf(EMPTY_BYTES));
        genesisBlock.setId(ByteArrayWrapper.copyOf(Base64.getDecoder().decode("AAbxHnNWxCDoYeGQygtqnNWIebwOiHnJkcpcAMdRvNM=")));
        genesisBlock.setDifficulty(INITIAL_DIFFICULTY);
        genesisBlock.setTimestamp(1498699423538l);
        genesisBlock.setTransactionRootHash(roottx);
        genesisBlock.setTransactionIds(txIds);
        genesisBlock.setTransactions(new ArrayList<byte[]>(Arrays.asList(tx.getOriginal())));
        genesisBlock.setParsedTransactions(new ArrayList<Transaction>(Arrays.asList(tx)));
        genesisBlock.setNonce(-7837543842364165229l);
        genesisBlock.setOriginal(BlockUtil.serialize(genesisBlock));

        logger.log("Genesis prepared.");
        System.out.println(genesisBlock.toJson());
    }

    public static Block getGenesisBlock() {
        return genesisBlock;
    }
}
