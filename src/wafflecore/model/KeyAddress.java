public class KeyAddress {
    public byte[] publicKey;
    public byte[] privateKey;
    public byte[] address;

    public KeyAddress(byte[] publicKey, byte[] privateKey, byte[] address) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.address = address;
    }
}
