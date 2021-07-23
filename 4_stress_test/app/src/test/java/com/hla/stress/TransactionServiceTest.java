package com.hla.stress;

import com.hla.stress.transaction.TransactionRepository;
import com.hla.stress.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


//@SpringBootTest
class TransactionServiceTest {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository repo;

    /*@Test
    void getTop10() {
        List<Transaction> allTransactions = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Transaction transaction = new Transaction();
            transaction.setValue(Math.abs(new Random().nextLong()));
            transaction.setUuid(UUID.randomUUID().toString());
            allTransactions.add(transaction);
        }
        repo.saveAllAndFlush(allTransactions);


        List<Transaction> top10 = transactionService.getTop10();
        assertNotNull(top10);
        assertEquals(10, top10.size());
    }*/
}