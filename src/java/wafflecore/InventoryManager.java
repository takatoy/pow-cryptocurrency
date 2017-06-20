package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.model.*;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryManager {
    public static ConcurrentHashMap<byte[], byte[]> blocks = new ConcurrentHashMap<byte[], byte[]>();
    public static ConcurrentHashMap<byte[], Transaction> memoryPool = new ConcurrentHashMap<byte[], Transaction>();
}
