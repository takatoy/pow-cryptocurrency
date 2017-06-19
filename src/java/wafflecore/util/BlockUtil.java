package wafflecore.util;

import wafflecore.model.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BlockUtil {
    public static Block deserializeBlock(byte[] data) {
        // WIP
        return null;
    }

    public static byte[] serializeBlock(Block block) {
        // WIP
        return null;
    }

    public static double difficultyOf(byte[] hash) {
        // WIP
        return 0;
    }

    public static long getCoinbaseAmount(int height) {
        if (height >= 2000) {
            return 0;
        } else {
            return 1000000 >> (height / 100);
        }
    }

    public static byte[] computeBlockId(byte[] data) {
        Block block = deserializeBlock(data);

        block.setTransactionIds(null);
        block.setTransactions(null);

        return Hasher.hash256(serializeBlock(block));
    }

    public static final int blocksToConsiderDifficulty = 3;
    public static double getNextDifficulty(ArrayList<Block> prevBlocks) {
        double lastDiff = prevBlocks.get(0).getDifficulty();
        if (prevBlocks.size() == 1) {
            return lastDiff;
        }

        long t = prevBlocks.get(0).getTimestamp() - prevBlocks.get(blocksToConsiderDifficulty).getTimestamp();
        double sumDiff = 0;
        for (int i = 0; i < blocksToConsiderDifficulty - 1; i++) {
            sumDiff += prevBlocks.get(i).getDifficulty();
        }
        double newDiff = sumDiff / t * TimeUnit.SECONDS.toMillis(30);

        // Next difficulty must be in range between +10% and -10%.
        if (newDiff < lastDiff * 0.9) newDiff = lastDiff * 0.9;
        if (newDiff > lastDiff * 1.1) newDiff = lastDiff * 1.1;

        return newDiff;
    }
}
