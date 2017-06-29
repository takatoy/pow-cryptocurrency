package waffle.wafflecore;

import static waffle.wafflecore.constants.Constants.*;
import waffle.wafflecore.model.*;
import waffle.wafflecore.util.ByteArrayWrapper;
import waffle.wafflecore.WaffleCore;
import waffle.wafflecore.message.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Callable;
import java.util.TreeMap;

public class Inventory {
    public static ConcurrentHashMap<ByteArrayWrapper, byte[]> blocks = new ConcurrentHashMap<ByteArrayWrapper, byte[]>();
    public static TreeMap<ByteArrayWrapper, Transaction> memoryPool = new TreeMap<ByteArrayWrapper, Transaction>();
}
