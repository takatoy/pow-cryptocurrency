package wafflecore.util;

import wafflecore.model.*;
import wafflecore.tool.SystemUtil;
import wafflecore.util.ByteArrayWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.nio.ByteBuffer;
import org.apache.commons.lang3.ArrayUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BlockUtil {
    public static ObjectMapper serializeMapper = new ObjectMapper();
    static {
        serializeMapper.addMixIn(Block.class, BlockMixIn.class);
    }

    public static Block deserializeBlock(byte[] data) {
        String dataStr = new String(data);

        // Json to block.
        Block block = null;
        try {
            block = serializeMapper.readValue(dataStr, Block.class);
            block.setOriginal(data);
            block.setId(computeBlockId(data));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return block;
    }

    public static byte[] serializeBlock(Block block) {
        byte[] serialized = null;

        try {
            serialized = serializeMapper.writeValueAsBytes(block);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serialized;
    }

    public static double difficultyOf(ByteArrayWrapper hash) {
        byte[] bytes = ArrayUtils.addAll(new byte[]{ (byte)0x3F, (byte)0xF0 }, hash.getBytes());
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        double d = buf.getDouble();

        return Math.pow(2, -35) / (d - 1);
    }

    public static long getCoinbaseAmount(int height) {
        if (height >= 2000) {
            return 0;
        } else {
            return 1000000 >> (height / 100);
        }
    }

    public static ByteArrayWrapper computeBlockId(byte[] data) {
        try {
            Block block = serializeMapper.readValue(data, Block.class);
            block.setTransactions(null);
            block.setTransactionIds(null);

            return ByteArrayWrapper.copyOf(Hasher.doubleSha256(serializeBlock(block)));
        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static final int blocksToConsiderDifficulty = 3;
    public static final int blockInterval = 30;
    public static double getNextDifficulty(ArrayList<Block> prevBlocks) {
        double lastDiff = prevBlocks.get(0).getDifficulty();
        if (prevBlocks.size() <= blocksToConsiderDifficulty) {
            return lastDiff;
        }

        long t = prevBlocks.get(0).getTimestamp() - prevBlocks.get(blocksToConsiderDifficulty).getTimestamp();
        double sumDiff = 0;
        for (int i = 0; i < blocksToConsiderDifficulty - 1; i++) {
            sumDiff += prevBlocks.get(i).getDifficulty();
        }
        double newDiff = sumDiff / t * TimeUnit.SECONDS.toMillis(blockInterval);

        // Next difficulty must be in range between +10% and -10%.
        if (newDiff < lastDiff * 0.9) newDiff = lastDiff * 0.9;
        if (newDiff > lastDiff * 1.1) newDiff = lastDiff * 1.1;

        return newDiff;
    }
}
