package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.model.*;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryManager {
    public ConcurrentHashMap<byte[], byte[]> blocks = new ConcurrentHashMap<byte[], byte[]>();
    public ConcurrentHashMap<byte[], Transaction> memoryPool = new ConcurrentHashMap<byte[], Transaction>();
}
