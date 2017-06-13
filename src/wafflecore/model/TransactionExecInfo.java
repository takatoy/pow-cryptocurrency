package wafflecore.model;

import java.util.ArrayList;

public class TransactionExecInfo {
    private boolean coinBase;
    private ArrayList<TransactionOutput> redeemedOutputs;
    private ArrayList<TransactionOutput> generatedOutputs;
    private long transactionFee;

    public TransactionExecInfo(
        boolean coinBase,
        ArrayList<TransactionOutput> redeemedOutputs,
        ArrayList<TransactionOutput> generatedOutputs,
        long transactionFee)
    {
        this.coinBase = coinBase;
        this.redeemedOutputs = redeemedOutputs;
        this.generatedOutputs = generatedOutputs;
        this.transactionFee = transactionFee;
    }

    // getter
    public boolean getCoinBase() {
        return coinBase;
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
    public void setCoinBase(boolean coinBase) {
        this.coinBase = coinBase;
    }
    public void setRedeemedOutputs(ArrayList<TransactionOutput> redeemedOutputs) {
        this.redeemedOutputs = redeemedOutputs;
    }
    public void setGeneratedOutputs(ArrayList<TransactionOutput> generatedOutputs) {
        this.generatedOutputs = generatedOutputs;
    }
    public void setTransactionFee(ArrayList<TransactionOutput> transactionFee) {
        this.transactionFee = transactionFee;
    }
}
