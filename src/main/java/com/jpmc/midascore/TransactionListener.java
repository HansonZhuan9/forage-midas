package com.jpmc.midascore;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.jpmc.midascore.foundation.Transaction;

@Service
public class TransactionListener {

    @KafkaListener(topics = "transactions", groupId = "midas-core")
    public void listen(Transaction transaction) {
        System.out.println(transaction.toString());
    }
}