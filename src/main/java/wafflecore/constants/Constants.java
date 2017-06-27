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
}
