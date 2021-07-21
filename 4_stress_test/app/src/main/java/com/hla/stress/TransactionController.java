package com.hla.stress;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping(value = "/write")
    public Transaction write(@RequestParam("uuid") String uuid, @RequestParam("value") Long value) {
        Transaction transaction = create(uuid, value);
        transactionService.save(transaction);
        return transaction;
    }

    @GetMapping(value = "/get")
    public Transaction get(@RequestParam("uuid") String uuid) {
        return transactionService.getByUuid(uuid);
    }

    @GetMapping(value = "/read")
    public List<Transaction> read(@RequestParam("threshold") Long threshold) {
        return transactionService.getAllGreaterThenThreshold(threshold);
    }

    private static Transaction create(String uuid, Long value) {
        Transaction transaction = new Transaction();
        transaction.setUuid(uuid);
        transaction.setValue(value);
        return transaction;
    }
}
