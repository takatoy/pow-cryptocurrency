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

        ConnectionManager connectionManager = new ConnectionManager("localhost", 9001);
        connectionManager.listen(); // listen

        System.out.println("Program still going.");
        try {
            Thread.sleep(5000);
        } catch (Exception e) {}

        connectionManager.asyncBroadcast("Hello?");
    }

    public static ExecutorService getExecutor() {
        return executor;
    }
}
