package wafflecore.model;

public class TransactionOutput {
    private byte[] transactionId;
    private short outIndex;
    private byte[] recipient;
    private long amount;

    public TransactionId(
        byte[] transactionId,
        short outIndex,
        byte[] recipient,
        long amount)
    {
        this.transactionId = transactionId;
        this.outIndex = outIndex;
        this.recipient = recipient;
        this.amount = amount;
    }

    // getter
    public byte[] getTransactionId() {
        return transactionId;
    }
    public short getOutIndex() {
        return outIndex;
    }
    public byte[] getRecipient() {
        return recipient;
    }
    public long getAmount() {
        return amount;
    }

    // setter
    public void setTransactionId(byte[] transactionId) {
        this.transactionId = transactionId;
    }
    public void setOutIndex(short outIndex) {
        this.outIndex = outIndex;
    }
    public void setRecipient(byte[] recipient) {
        this.recipient = recipient;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }
}
