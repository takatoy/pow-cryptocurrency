package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.model.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.TreeMap;

public class InventoryManager {
    public static ConcurrentHashMap<byte[], byte[]> blocks = new ConcurrentHashMap<byte[], byte[]>();
    public static TreeMap<byte[], Transaction> memoryPool = new TreeMap<byte[], Transaction>();
}
