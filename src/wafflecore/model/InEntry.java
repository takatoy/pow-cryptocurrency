package wafflecore.model;

public class InEntry {
    private byte[] transactionId;
    private short outEntryIndex;
    private byte[] publicKey;
    private byte[] signature;

    public InEntry(
        byte[] transactionId,
        short outEntryIndex,
        byte[] publicKey,
        byte[] signature)
    {
        this.transactionId = transactionId;
        this.outEntryIndex = outEntryIndex;
        this.publicKey = publicKey;
        this.signature = signature;
    }

    // getter
    public byte[] getTransactionId() {
        return transactionId;
    }
    public short getOutEntryIndex() {
        return outEntryIndex;
    }
    public byte[] getPublicKey() {
        return publicKey;
    }
    public byte[] getSignature() {
        return signature;
    }

    // setter
    public void setTransactionId(byte[] transactionId)  {
        this.transactionId = transactionId;
    }
    public void setOutEntryIndex(short outEntryIndex) {
        this.outEntryIndex = outEntryIndex;
    }
    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
