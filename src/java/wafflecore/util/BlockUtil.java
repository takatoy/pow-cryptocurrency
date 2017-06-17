package wafflecore.util;

import wafflecore.model.*;
import java.util.ArrayList;

public class BlockUtil {
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

    public static double getNextDifficulty(ArrayList<Block> previousBlocks) {
        return 0;
    }
}
