package wafflecore.constants;

/**
 *  All the global constant values must be written here.
 *  import static wafflecore.WaffleConstants.*;
 */
public final class Constants {
    private Constants(){}

    public static final String ROOT_DIR = System.getProperty("user.dir");
    public static final String DATA_DIR = System.getProperty("user.home") + "/.waffle";

    public static final byte[] EMPTY_BYTES = new byte[32];
    public static final int MAX_BLOCK_SIZE = 1024 * 1024; // 1MB
    public static final int HASH_LENGTH = 32;

    // Message enums.
    public static final int MSG_TYPE_HELLO = 0;
    public static final int MSG_TYPE_INVENTORY = 1;

    // Inventory message enums.
    public static final int MSG_TYPE_ADVERTISE = 0;
    public static final int MSG_TYPE_REQUEST = 1;
    public static final int MSG_TYPE_CONTENT = 2;
}
