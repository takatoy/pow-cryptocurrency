package wafflecore.util;

import static wafflecore.constants.Constants.*;
import wafflecore.util.Hasher;
import wafflecore.tool.SystemUtil;
import wafflecore.model.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashSet;

public class BlockChainUtil {
    public static ArrayList<Block> ancestors(Block block, ConcurrentHashMap<byte[], Block> blocks) {
        ArrayList<Block> ret = new ArrayList<Block>();
        byte[] id = block.getId();

        System.out.println("----Blocks");
        blocks.forEach(1, (key, val) -> System.out.println(SystemUtil.bytesToStr(key)));

        System.out.println("----ancestors");
        while (id != null) {
            System.out.println(SystemUtil.bytesToStr(id));
            Block blk = blocks.get(id);
            System.out.println(blk);
            if (blk == null) break;

            ret.add(blk);
            id = blk.getPreviousHash();
        }
        System.out.println("--------");

        return ret;
    }

    public static Block lowestCommonAncestor(Block b1, Block b2, ConcurrentHashMap<byte[], Block> blocks) {
        HashSet<byte[]> appeared = new HashSet<byte[]>();

        ArrayList<Block> a1 = ancestors(b1, blocks);
        ArrayList<Block> a2 = ancestors(b2, blocks);

        int len = Math.max(a1.size(), a2.size());
        for (int i = 0; i < len; i++) {
            System.out.println("A1 "+BlockUtil.blockIdStr(a1.get(i))+":"+a1.get(i).toJson());
            System.out.println("A2 "+BlockUtil.blockIdStr(a2.get(i))+":"+a2.get(i).toJson());
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

    public static byte[] rootHashTransactionIds(ArrayList<byte[]> txIds) {
        byte[] ids = new byte[txIds.size() * HASH_LENGTH];
        int i = 0;
        for (byte[] txId : txIds) {
            System.arraycopy(txId, 0, ids, i * HASH_LENGTH, HASH_LENGTH);
            i++;
        }

        return Hasher.doubleSha256(ids);
    }

    public static byte[] toAddress(byte[] publicKey) {
        return Hasher.doubleSha256(publicKey);
    }
}
