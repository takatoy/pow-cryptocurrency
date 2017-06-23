package wafflecore.model;

public class TransactionOutput {
    private ByteArrayWrapper transactionId;
    private short outIndex;
    private ByteArrayWrapper recipient;
    private long amount;

    public TransactionOutput() {
        this.transactionId = new byte[32];
        this.outIndex = 0;
        this.recipient = new byte[32];
        this.amount = 0;
    }

    public TransactionOutput(
        ByteArrayWrapper transactionId,
        short outIndex,
        ByteArrayWrapper recipient,
        long amount)
    {
        this.transactionId = transactionId;
        this.outIndex = outIndex;
        this.recipient = recipient;
        this.amount = amount;
    }

    // getter
    public ByteArrayWrapper getTransactionId() {
        return transactionId;
    }
    public short getOutIndex() {
        return outIndex;
    }
    public ByteArrayWrapper getRecipient() {
        return recipient;
    }
    public long getAmount() {
        return amount;
    }

    // setter
    public void setTransactionId(ByteArrayWrapper transactionId) {
        this.transactionId = transactionId;
    }
    public void setOutIndex(short outIndex) {
        this.outIndex = outIndex;
    }
    public void setRecipient(ByteArrayWrapper recipient) {
        this.recipient = recipient;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }
}
