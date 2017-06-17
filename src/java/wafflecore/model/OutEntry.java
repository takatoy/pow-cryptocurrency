package wafflecore.model;

public class OutEntry {
    private byte[] recipientHash;
    private long amount;

    public OutEntry(
        byte[] recipientHash,
        long amount)
    {
        this.recipientHash = recipientHash;
        this.amount = amount;
    }

    // getter
    public byte[] getRecipientHash() {
        return recipientHash;
    }
    public long getAmount() {
        return amount;
    }

    // setter
    public void setRecipientHash(byte[] recipientHash) {
        this.recipientHash = recipientHash;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }
}
