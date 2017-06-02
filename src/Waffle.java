import wafflecore.*;

class Waffle {
    public static void main(String[] args) {
        WaffleHttpServer httpServer = new WaffleHttpServer();
        httpServer.start();
        WaffleSystem.writeLog("Server started.");
    }
}
