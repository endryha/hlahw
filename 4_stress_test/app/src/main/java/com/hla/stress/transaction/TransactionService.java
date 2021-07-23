package com.hla.stress.transaction;

import java.util.List;

public interface TransactionService {
    Transaction getByUuid(String uuid);

    void save(Transaction transaction);

    List<Transaction> getTop10();
}
