package wafflecore.model;

import java.util.ArrayList;

public class Block {
    private byte[] original; // Original Block bytes
    private byte[] id;
    private String previousHash;
    private double difficulty;
    private long nonce;
    private long timestamp;
    private byte[] transactionRootHash;
    private ArrayList<byte[]> transactionIds;
    private ArrayList<byte[]> transactions;
    private int height;
    private Transaction parsedTransactions;
    private double totalDifficulty;
    private String data;
    private String hash;

    public Block(
        byte[] original,
        byte[] id,
        String previousHash,
        double difficulty,
        long nonce,
        long timestamp,
        byte[] transactionRootHash,
        ArrayList<byte[]> transactionIds,
        ArrayList<byte[]> transactions,
        int height,
        Transaction parsedTransactions,
        double totalDifficulty)
    {
        this.original, = original;
        this.id, = id;
        this.previousHash, = previousHash;
        this.difficulty, = difficulty;
        this.nonce, = nonce;
        this.timestamp, = timestamp;
        this.transactionRootHash, = transactionRootHash;
        this.transactionIds, = transactionIds;
        this.transactions, = transactions;
        this.height, = height;
        this.parsedTransactions, = parsedTransactions;
        this.totalDifficulty = totalDifficult;
    }

    // getter
    public byte[] getOriginal() {
        return original;
    }
    public byte[] getId() {
        return id;
    }
    public String getPreviousHash() {
        return previousHash;
    }
    public double getDifficulty() {
        return difficulty;
    }
    public long getNonce() {
        return nonce;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public byte[] getTransactionRootHash() {
        return transactionRootHash;
    }
    public ArrayList<byte[]> getTransactionIds() {
        return transactionIds;
    }
    public ArrayList<byte[]> getTransactions() {
        return transactions;
    }
    public int getHeight() {
        return height;
    }
    public Transaction getParsedTransactions() {
        return parsedTransactions;
    }
    public double getTotalDifficulty() {
        return totalDifficulty;
    }
    public String getData() {
        return data;
    }
    public String getHash() {
        return hash;
    }

    // setter
    public void setOriginal(byte[] original) {
        this.original = original;
    }
    public void setId(byte[] id) {
        this.id = id;
    }
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }
    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }
    public void setNonce(long nonce) {
        this.nonce = nonce;
    }
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    public void setTransactionRootHash(byte[] transactionRootHash) {
        this.transactionRootHash = transactionRootHash;
    }
    public void setTransactionIds(ArrayList<byte[]> transactionIds) {
        this.transactionIds = transactionIds;
    }
    public void setTransactions(ArrayList<byte[]> transactions) {
        this.transactions = transactions;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public void setParsedTransactions(Transaction parsedTransactions) {
        this.parsedTransactions = parsedTransactions;
    }
    public void setTotalDifficulty(double totalDifficulty) {
        this.totalDifficulty = totalDifficulty;
    }
    public void setData(String data) {
        this.data = data;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
}
