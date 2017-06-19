package wafflecore;

import wafflecore.tool.Logger;
import wafflecore.model.*;
import wafflecore.util.BlockUtil;
import java.security.SecureRandom;
import java.nio.ByteBuffer;

public class Miner {
    private Logger logger = Logger.getInstance();
    private static boolean isMining = true;

    public static boolean mine(Block seed) {
        SecureRandom random = new SecureRandom();
        byte[] nonceSeed = new byte[Long.BYTES];
        random.nextBytes(nonceSeed);

        long nonce = ByteBuffer.wrap(nonceSeed).getLong(); // Byte array to Long
        while (isMining) {
            seed.setNonce(nonce++);
            seed.setTimestamp(System.currentTimeMillis());

            byte[] data = BlockUtil.serializeBlock(seed);
            byte[] blockId = BlockUtil.computeBlockId(data);

            if (BlockUtil.difficultyOf(blockId) > seed.getDifficulty()) {
                seed.setId(blockId);
                seed.setOriginal(data);
                return true;
            }
        }

        return false;
    }
}
