package wafflecore.model;

import static wafflecore.constants.Constants.*;
import wafflecore.util.ByteArrayWrapper;

public class TransactionOutput {
    private ByteArrayWrapper transactionId;
    private short outIndex;
    private byte[] recipient;
    private long amount;

    public TransactionOutput() {
        this.transactionId = ByteArrayWrapper.copyOf(EMPTY_BYTES);
        this.outIndex = 0;
        this.recipient = null;
        this.amount = 0;
    }

    public TransactionOutput(
        ByteArrayWrapper transactionId,
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
    public ByteArrayWrapper getTransactionId() {
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
    public void setTransactionId(ByteArrayWrapper transactionId) {
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
