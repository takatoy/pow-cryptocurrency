package wafflecore.util;

import wafflecore.model.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class BlockChainUtil {
    public static ArrayList<Block> ancestors(Block block, ConcurrentHashMap<byte[], Block> blocks) {
        // WIP
        return new ArrayList<Block>();
    }

    public static Block lowestCommonAncestor(Block b1, Block b2, ConcurrentHashMap<byte[], Block> blocks) {
        // WIP
        return null;
    }

    public static byte[] rootHashTransactionIds(ArrayList<byte[]> txIds) {
        return new byte[32];
    }

    public static byte[] toAddress(byte[] publicKey) {
        return Hasher.hash256(publicKey);
    }
}
