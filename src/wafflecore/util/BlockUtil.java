package wafflecore.util;

import wafflecore.model.*;

class BlockUtil {
    public static Block deserializeBlock(byte[] bytes) {
        // WIP
        return null;
    }

    public static long getCoinbaseAmount(int height) {
        if (height >= 2000) {
            return 0;
        } else {
            return 1000000 >> (height / 100);
        }
    }

    public static double getNextDifficulty(ArrayList<Blocks> previousBlocks) {
        previousBlocks.add();
    }
}
