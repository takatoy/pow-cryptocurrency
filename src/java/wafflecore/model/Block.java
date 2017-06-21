package wafflecore.model;

import static wafflecore.constants.Constants.*;

import java.util.ArrayList;

public class Block {
    private byte[] original; // Original Block bytes
    private byte[] id;
    private byte[] previousHash;
    private double difficulty;
    private long nonce;
    private long timestamp;
    private byte[] transactionRootHash;
    private ArrayList<byte[]> transactionIds;
    private ArrayList<byte[]> transactions;
    private int height;
    private ArrayList<Transaction> parsedTransactions;
    private double totalDifficulty;

    public Block() {
        this.original = null;
        this.id = null;
        this.previousHash = null;
        this.difficulty = 0;
        this.nonce = 0;
        this.timestamp = 0;
        this.transactionRootHash = null;
        this.transactionIds = null;
        this.transactions = null;
        this.height = 0;
        this.parsedTransactions = null;
        this.totalDifficulty = 0;
    }

    // getter
    public byte[] getOriginal() {
        return original;
    }
    public byte[] getId() {
        return id;
    }
    public byte[] getPreviousHash() {
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
    public ArrayList<Transaction> getParsedTransactions() {
        return parsedTransactions;
    }
    public double getTotalDifficulty() {
        return totalDifficulty;
    }

    // setter
    public void setOriginal(byte[] original) {
        this.original = original;
    }
    public void setId(byte[] id) {
        this.id = id;
    }
    public void setPreviousHash(byte[] previousHash) {
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
    public void setParsedTransactions(ArrayList<Transaction> parsedTransactions) {
        this.parsedTransactions = parsedTransactions;
    }
    public void setTotalDifficulty(double totalDifficulty) {
        this.totalDifficulty = totalDifficulty;
    }
}
