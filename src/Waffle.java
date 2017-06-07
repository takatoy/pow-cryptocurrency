import wafflecore.*;

class Waffle {
    public static void main(String[] args) throws Exception {
        // WaffleHttpServer httpServer = new WaffleHttpServer();
        // httpServer.start();
        // WaffleSystem.writeLog("Server started.");

        byte[] bytes = "Hello, world".getBytes();
        byte[] hashed = WaffleHash.hash256(bytes);
        System.out.println(WaffleSystem.bytesToStr(hashed));
        System.out.println(WaffleBase58.encode(hashed));
    }
}
