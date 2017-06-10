package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.connection.Listener;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class WaffleCore {
    public static void main(String[] args) {
        File dataDir = new File(DATA_DIR);
        dataDir.mkdir();

        ExecutorService worker = Executors.newCachedThreadPool();

        Listener listener = new Listener("localhost", 9001, true);
        worker.submit(listener);
    }
}
