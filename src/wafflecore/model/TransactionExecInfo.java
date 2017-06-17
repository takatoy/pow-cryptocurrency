package wafflecore.model;

import java.util.ArrayList;

public class TransactionExecInfo {
    private boolean coinbase;
    private ArrayList<TransactionOutput> redeemedOutputs;
    private ArrayList<TransactionOutput> generatedOutputs;
    private long transactionFee;

    public TransactionExecInfo(
        boolean coinbase,
        ArrayList<TransactionOutput> redeemedOutputs,
        ArrayList<TransactionOutput> generatedOutputs,
        long transactionFee)
    {
        this.coinbase = coinbase;
        this.redeemedOutputs = redeemedOutputs;
        this.generatedOutputs = generatedOutputs;
        this.transactionFee = transactionFee;
    }

    // getter
    public boolean getCoinbase() {
        return coinbase;
    }
    public ArrayList<TransactionOutput> getRedeemedOutputs() {
        return redeemedOutputs;
    }
    public ArrayList<TransactionOutput> getGeneratedOutputs() {
        return generatedOutputs;
    }
    public long getTransactionFee() {
        return transactionFee;
    }

    // setter
    public void setCoinbase(boolean coinbase) {
        this.coinbase = coinbase;
    }
    public void setRedeemedOutputs(ArrayList<TransactionOutput> redeemedOutputs) {
        this.redeemedOutputs = redeemedOutputs;
    }
    public void setGeneratedOutputs(ArrayList<TransactionOutput> generatedOutputs) {
        this.generatedOutputs = generatedOutputs;
    }
    public void setTransactionFee(long transactionFee) {
        this.transactionFee = transactionFee;
    }
}
