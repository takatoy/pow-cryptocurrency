package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.ConnectionManager;

import java.io.File;
import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;

public class WaffleCore {
    private static ExecutorService executor = null; // Thread Executor

    public static void main(String[] args) throws Exception {
        File dataDir = new File(DATA_DIR);
        dataDir.mkdir();

        // Initiate thread executor
        executor = Executors.newCachedThreadPool();

        ConnectionManager cn = new ConnectionManager();
        Future<Void> listener = cn.listen("localhost", 9001); // listen

        System.out.println("Program still going.");

        // try {
        //     Thread.sleep(5000);
        // } catch(Exception e) {}
    }

    public static ExecutorService getExecutor() {
        return executor;
    }
}
