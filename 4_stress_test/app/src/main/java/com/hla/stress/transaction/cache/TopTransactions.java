package com.hla.stress.transaction.cache;

import com.hla.stress.transaction.Transaction;

import java.util.List;

public class TopTransactions {
    private List<Transaction> transactions;
    private long timeToFetch;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public long getTimeToFetch() {
        return timeToFetch;
    }

    public void setTimeToFetch(long timeToFetch) {
        this.timeToFetch = timeToFetch;
    }
}
