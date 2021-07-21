package com.hla.stress;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository repo;

    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }

    public List<Transaction> getAllGreaterThenThreshold(long threshold) {
        return repo.findAllByValueIsGreaterThan(threshold);
    }

    public Transaction getByUuid(String uuid) {
        return repo.findFirstByUuid(uuid);
    }

    public List<Transaction> listAll() {
        return repo.findAll();
    }

    public void save(Transaction transaction) {
        repo.save(transaction);
    }
}
