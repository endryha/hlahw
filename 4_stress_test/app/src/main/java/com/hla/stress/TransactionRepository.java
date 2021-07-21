package com.hla.stress;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByValueIsGreaterThan(long threshold);

    Transaction findFirstByUuid(String uuid);
}
