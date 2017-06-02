package wafflecore;

import java.util.scanner;

class WaffleNode {
    WaffleSocket waffleSocket;

    public WaffleNode(WaffleSocket waffleSocket) {
        this.waffleSocket = waffleSocket;
    }

    public static WaffleBlock getGenesisBlock() {
        return new WaffleBlock(0, "0", 1495781049, "GenesisBlock", "3ff1d5ad18e3a5c8788b93218290a238aecf76f7b96b07ef2cfcd9eb6c93042a");
    }
}
