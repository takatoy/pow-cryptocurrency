package wafflecore.util;

import static wafflecore.constants.Constants.*;
import wafflecore.util.Hasher;
import wafflecore.tool.SystemUtil;
import wafflecore.model.*;
import wafflecore.util.ByteArrayWrapper;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;

public class BlockChainUtil {
    public static ArrayList<Block> ancestors(Block block, ConcurrentHashMap<ByteArrayWrapper, Block> blocks) {
        ArrayList<Block> ret = new ArrayList<Block>();
        ByteArrayWrapper id = block.getId();

        while (id != null) {
            Block blk = blocks.get(id);
            if (blk == null) break;

            ret.add(blk);
            id = blk.getPreviousHash();
        }

        return ret;
    }

    public static Block lowestCommonAncestor(Block b1, Block b2, ConcurrentHashMap<ByteArrayWrapper, Block> blocks) {
        HashSet<ByteArrayWrapper> appeared = new HashSet<ByteArrayWrapper>();

        ArrayList<Block> a1 = ancestors(b1, blocks);
        ArrayList<Block> a2 = ancestors(b2, blocks);

        int len = Math.max(a1.size(), a2.size());
        for (int i = 0; i < len; i++) {
            if (a1.size() > i) {
                if (appeared.contains(a1.get(i).getId())) {
                    return a1.get(i);
                } else {
                    appeared.add(a1.get(i).getId());
                }
            }

            if (a2.size() > i) {
                if (appeared.contains(a2.get(i).getId())) {
                    return a2.get(i);
                } else {
                    appeared.add(a2.get(i).getId());
                }
            }
        }

        return null;
    }

    public static byte[] rootHashTransactionIds(ArrayList<ByteArrayWrapper> txIds) {
        // Using string to concat bytes.
        String ids = "";
        for (ByteArrayWrapper txId : txIds) {
            ids += txId.toString();
        }

        return Hasher.doubleSha256(ids.getBytes());
    }

    public static byte[] toAddress(byte[] publicKey) {
        return Hasher.doubleSha256(publicKey);
    }
}
