package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.model.*;
import wafflecore.util.ByteArrayWrapper;
import wafflecore.WaffleCore;
import wafflecore.message.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Callable;
import java.util.TreeMap;

public class Inventory {
    public static ConcurrentHashMap<ByteArrayWrapper, byte[]> blocks = new ConcurrentHashMap<ByteArrayWrapper, byte[]>();
    public static TreeMap<ByteArrayWrapper, Transaction> memoryPool = new TreeMap<ByteArrayWrapper, Transaction>();
}
