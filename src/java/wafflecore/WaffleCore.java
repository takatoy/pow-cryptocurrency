import wafflecore.*;
import static wafflecore.constants.WaffleConstants.*;

import java.net.*;

class WaffleCore {
    public static void main(String[] args) throws Exception {
        // WaffleHttpServer httpServer = new WaffleHttpServer();
        // httpServer.start();
        // WaffleSystem.writeLog("Server started.");

        // String org = "Higuchi Yosuke";
        // System.out.println("ORIGINAL: " + org);
        // byte[] bytes = org.getBytes();
        // byte[] hashed = WaffleHash.hash256(bytes);
        // System.out.println("SHA256: " + WaffleSystem.bytesToStr(hashed));
        // String encoded = WaffleBase58.encode(hashed);
        // System.out.println("BASE58: " + encoded);
        // byte[] decoded = WaffleBase58.decode(encoded);
        // System.out.println("SHA256: " + WaffleSystem.bytesToStr(decoded));
        // System.out.println(ROOT_DIR);

        Listener listener = new Listener("127.0.0.1", 9001);
        listener.start();

        System.out.println("Program is still going");
    }
}
