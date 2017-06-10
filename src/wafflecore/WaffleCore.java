package wafflecore;

import static wafflecore.constants.Constants.*;
import wafflecore.connection.Listener;

import java.io.File;

class WaffleCore {
    public static void main(String[] args) {
        File dataDir = new File(DATA_DIR);
        dataDir.mkdir();

        Listener listener = new Listener("127.0.0.1", 9001);
        listener.start();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {}

        listener.stopRunning();
    }
}
