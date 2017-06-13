package wafflecore.model;

public class KeyPair {
    public byte[] publicKey;
    public byte[] privateKey;
    public byte[] address;

    public KeyPair(byte[] publicKey, byte[] privateKey, byte[] address) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.address = address;
    }
}
