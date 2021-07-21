package com.hla.stress;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/write", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Transaction write(@RequestParam("uuid") String uuid, @RequestParam("value") Long value) {
        Transaction transaction = new Transaction();
        transaction.setUuid(uuid);
        transaction.setValue(value);

        transactionService.save(transaction);

        return transaction;
    }
}
