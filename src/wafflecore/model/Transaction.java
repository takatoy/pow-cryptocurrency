package wafflecore.model;

import java.util.ArrayList;

public class Transaction {
    private byte[] original;
    private byte[] id;
    private long timestamp;
    private ArrayList<InEntry> inEntries;
    private ArrayList<OutEntry> outEntries;
    private TransactionExecInfo execInfo;

    public Transaction(
        byte[] original;
        byte[] id,
        long timestamp,
        ArrayList<InEntry> inEntries,
        ArrayList<OutEntry> outEntries,
        TransactionExecInfo execInfo)
    {
            this.original = original;
            this.id = id;
            this.timestamp = timestamp;
            this.inEntries = inEntries;
            this.outEntries = outEntries;
            this.execInfo = execInfo;
    }

    // getter
    public byte[] getOriginal() {
        return original;
    }
    public byte[] getId() {
        return id;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public ArrayList<InEntry> getInEntries() {
        return inEntries;
    }
    public ArrayList<OutEntry> getOutEntries() {
        return outEntries;
    }
    public TransactionExecInfo getExecInfo() {
        return execInfo;
    }

    // setter
    public void setOriginal(byte[] original) {
        this.original = original;
    }
    public void setId(byte[] id) {
        this.id = id;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public void setInEntries(ArrayList<InEntry> inEntries) {
        this.inEntries = inEntries;
    }
    public void setOutEntries(ArrayList<OutEntry> outEntries) {
        this.outEntries = outEntries;
    }
    public void setExecInfo(TransactionExecInfo execInfo) {
        this.execInfo = execInfo;
    }
}
