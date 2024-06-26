package com.casino.backend.service;


import com.casino.backend.entity.Player;
import com.casino.backend.entity.Transaction;
import com.casino.backend.enums.TransactionType;
import com.casino.backend.exception.InsufficientBalanceException;
import com.casino.backend.exception.InvalidTransactionException;
import com.casino.backend.exception.PlayerNotFoundException;
import com.casino.backend.exception.PlayerUserNameNotFoundException;
import com.casino.backend.repository.PlayerRepository;
import com.casino.backend.repository.TransactionRepository;
import com.casino.backend.request.UpdateBalanceRequest;
import com.casino.backend.response.BalanceResponse;
import com.casino.backend.response.Last10TransactionResponse;
import com.casino.backend.response.UpdateBalanceResponse;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@AllArgsConstructor
@Service
public class PlayerService {

    private static final Logger logger = LogManager.getLogger(PlayerService.class);
    /**
     * To ensure that the system handles multiple concurrent transactions for the same player correctly,
     * I use a lock to prevent concurrent transactions from accessing the same player's balance simultaneously.
     * I use a ReentrantLock or a Lock object from the java.util.concurrent.locks package.
     */
    private final ReentrantLock lock = new ReentrantLock();

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TransactionRepository transactionRepository;


    public BalanceResponse getBalance(Integer playerId) {
        var player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("The player ID you provided is not valid. Please enter a valid player ID."));

        logger.info("Player: {}", player);
        return BalanceResponse
                .builder()
                .playerId(player.getPlayerId())
                .balance(player.getBalance())
                .build();
    }

    public Player getPlayerByUsername(String username) {
        return playerRepository.findByUsername(username)
                .orElseThrow(() -> new PlayerUserNameNotFoundException("The username you provided is not recognized. Please enter a valid username."));
    }

    public UpdateBalanceResponse updateBalance(Integer playerId, UpdateBalanceRequest request) {
        lock.lock();
        try {
            var player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new PlayerNotFoundException("The player ID you provided is not valid. Please enter a valid player ID."));

            logPlayerInfo(player);
            validateRequest(request, player);
            var newBalance = calculateNewBalance(request, player);

            player.setBalance(newBalance);

            var transaction = buildTransaction(player, request.getTransactionType(), newBalance);

            var savedTransaction = transactionRepository.save(transaction);

            return UpdateBalanceResponse.builder()
                    .transactionId(savedTransaction.getTransactionId())
                    .balance(player.getBalance())
                    .build();

        } finally {
            lock.unlock();
        }
    }

    private void logPlayerInfo(Player player) {
        logger.info("UpdateBalanceResponse Player: {}", player);
    }

    private void validateRequest(UpdateBalanceRequest request, Player player) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("The amount must be a positive value. Please enter a valid amount greater than zero.");
        }

        if (TransactionType.WAGER.equals(request.getTransactionType()) && player.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("You do not have sufficient balance to place this wager. Please adjust your wager to be within your available balance.");
        }

        logger.info("UpdateBalanceResponse current balance: {}", player.getBalance());
    }

    private BigDecimal calculateNewBalance(UpdateBalanceRequest request, Player player) {
        var newBalance = TransactionType.WAGER.equals(request.getTransactionType())
                ? player.getBalance().subtract(request.getAmount())
                : player.getBalance().add(request.getAmount());

        logger.info("UpdateBalanceResponse new balance: {}", newBalance);
        return newBalance;
    }

    private Transaction buildTransaction(Player player, TransactionType transactionType, BigDecimal amount) {
        return Transaction.builder()
                .player(player)
                .amount(amount)
                .transactionType(transactionType)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public List<Last10TransactionResponse> getLast10Transactions(Player player) {
        List<Transaction> transactions = transactionRepository.findTop10ByPlayerOrderByTimestampDesc(player);
        List<Last10TransactionResponse> last10TransactionResponseList = new ArrayList<>();
        for (var transaction : transactions) {
            var transactionResponse = Last10TransactionResponse
                    .builder()
                    .transactionType(transaction.getTransactionType())
                    .transactionId(transaction.getTransactionId())
                    .amount(transaction.getAmount())
                    .build();
            last10TransactionResponseList.add(transactionResponse);
        }
        logger.info("Top: {} transactions: {}", last10TransactionResponseList.size(), last10TransactionResponseList);
        return last10TransactionResponseList;
    }
}
