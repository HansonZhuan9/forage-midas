package com.jpmc.midascore;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.repository.TransactionRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionListener {
    private static final Logger logger = LoggerFactory.getLogger(TransactionListener.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @KafkaListener(topics = "transactions", groupId = "midas-core")
    public void listen(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (sender == null || recipient == null) {
            logger.warn("Invalid sender or recipient: {}", transaction);
            return;
        }

        if (sender.getBalance() < transaction.getAmount()) {
            logger.warn("Insufficient balance: {}", transaction);
            return;
        }

        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction,
                Incentive.class
        );

        float incentiveAmount = (incentive != null) ? incentive.getAmount() : 0f;


        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);
        userRepository.save(sender);
        userRepository.save(recipient);

        TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);
        transactionRepository.save(record);

        logger.info("Transaction recorded with incentive {}: {}", incentiveAmount, transaction);

        UserRecord a = userRepository.findById(1L);
        UserRecord b = userRepository.findById(2L);
        UserRecord c = userRepository.findById(3L);
        UserRecord d = userRepository.findById(4L);
        UserRecord e = userRepository.findById(5L);
        UserRecord f = userRepository.findById(6L);
        UserRecord g = userRepository.findById(7L);
        UserRecord h = userRepository.findById(8L);
        UserRecord i = userRepository.findById(9L);
        UserRecord j = userRepository.findById(10L);
        logger.info("{}{}{}{}{}{}{}{}{}{}", a, b,c,d,e,f,g,h,i,j);

    }
}