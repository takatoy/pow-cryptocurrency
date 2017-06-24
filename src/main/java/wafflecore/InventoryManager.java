package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.model.*;
import wafflecore.util.ByteArrayWrapper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.TreeMap;

public class InventoryManager {
    public static ConcurrentHashMap<ByteArrayWrapper, byte[]> blocks = new ConcurrentHashMap<ByteArrayWrapper, byte[]>();
    public static TreeMap<ByteArrayWrapper, Transaction> memoryPool = new TreeMap<ByteArrayWrapper, Transaction>();
}
