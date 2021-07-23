package com.hla.stress;

import com.hla.stress.transaction.Transaction;
import com.hla.stress.transaction.TransactionRepository;
import com.hla.stress.transaction.TransactionService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DbTransactionService implements TransactionService {

    private final TransactionRepository repo;

    public DbTransactionService(TransactionRepository repo) {
        this.repo = repo;
    }

    @Override
    public Transaction getByUuid(String uuid) {
        return repo.findFirstByUuid(uuid);
    }

    @Override
    public void save(Transaction transaction) {
        repo.save(transaction);
    }

    @Override
    public List<Transaction> getTop10() {
        return repo.findTop10ByOrderByValueDesc();
    }
}
