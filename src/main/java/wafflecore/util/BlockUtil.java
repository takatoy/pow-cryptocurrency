package wafflecore.util;

import wafflecore.model.*;
import wafflecore.tool.SystemUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.nio.ByteBuffer;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BlockUtil {
    public static Block deserializeBlock(byte[] data) {
        String dataStr = new String(data);

        // Json to block.
        ObjectMapper mapper = new ObjectMapper();
        Block block = null;
        try {
            block = mapper.readValue(dataStr, Block.class);
            block.setOriginal(data);
            block.setId(computeBlockId(data));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return block;
    }

    public static byte[] serializeBlock(Block block) {
        return block.toJson().getBytes();
    }

    public static String blockIdStr(Block block) {
        if (block == null) return "";

        byte[] id = block.getId();
        return SystemUtil.bytesToStr(id);
    }

    public static double difficultyOf(byte[] hash) {
        ByteBuffer bytes = ByteBuffer.allocate(256);
        bytes.put((byte)0x3F);
        bytes.put((byte)0xF0);
        bytes.put(hash);

        double d = bytes.getDouble();

        // return Math.pow(2, -35) / (d - 1)
        return 5.0;
    }

    public static long getCoinbaseAmount(int height) {
        if (height >= 2000) {
            return 0;
        } else {
            return 1000000 >> (height / 100);
        }
    }

    public static ByteArrayWrapper computeBlockId(byte[] data) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            Block block = mapper.readValue(data, Block.class);
            block.setParsedTransactions(null);

            return ByteArrayWrapper.copyOf(Hasher.doubleSha256(serializeBlock(block)));
        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
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
