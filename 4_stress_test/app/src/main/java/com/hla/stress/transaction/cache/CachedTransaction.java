package com.hla.stress.transaction.cache;

import com.hla.stress.transaction.Transaction;

class CachedTransaction {
    private Transaction transaction;
    private int delta;

    public CachedTransaction(Transaction transaction, int delta) {
        this.transaction = transaction;
        this.delta = delta;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }
}
