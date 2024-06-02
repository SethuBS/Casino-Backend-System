package com.casino.backend.bootstrap;

import com.casino.backend.entity.Player;
import com.casino.backend.entity.Transaction;
import com.casino.backend.enums.TransactionType;
import com.casino.backend.repository.PlayerRepository;
import com.casino.backend.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Random;

@AllArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(DataLoader.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public void run(String... args) {
        // Create a single player
        var newPlayer = new Player(
                null,
                "test_player",
                new BigDecimal("1000.00")

        );

        var savedPlayer = playerRepository.save(newPlayer);
        logger.info("saved player: {}", savedPlayer);

        // Create 11 transactions
        Random random = new Random();
        for (int i = 0; i < 11; i++) {
            var transaction = new Transaction();
            transaction.setPlayer(savedPlayer);
            transaction.setAmount(BigDecimal.valueOf(random.nextDouble() * 100).setScale(2, RoundingMode.HALF_UP));
            transaction.setTransactionType(random.nextBoolean() ? TransactionType.WAGER : TransactionType.WIN);
            transaction.setTimestamp(LocalDateTime.now().minusDays(random.nextInt(30)));
            transactionRepository.save(transaction);
            logger.info("saved transaction: {}", transaction);
            var newBalance = TransactionType.WAGER.equals(transaction.getTransactionType())
                    ? newPlayer.getBalance().subtract(transaction.getAmount())
                    : newPlayer.getBalance().add(transaction.getAmount());
            newPlayer.setBalance(newBalance);
            playerRepository.save(newPlayer);

        }
    }
}
