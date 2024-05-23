package com.casino.backend.service;


import com.casino.backend.entity.Player;
import com.casino.backend.entity.Transaction;
import com.casino.backend.enums.TransactionType;
import com.casino.backend.repository.PlayerRepository;
import com.casino.backend.repository.TransactionRepository;
import com.casino.backend.request.UpdateBalanceRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {

    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() {
        // Initialize the player repository and transaction repository
        Player player = Player.builder().playerId(1).username("test_user").balance(BigDecimal.valueOf(1000)).build();
        when(playerRepository.findById(1)).thenReturn(Optional.of(player));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());
    }

    @Test
    public void testUpdateBalanceConcurrency() throws Exception {
        // Create a thread pool with 5 threads
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Submit the tasks to the thread pool
        List<Runnable> tasks = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            tasks.add(() -> playerService.updateBalance(1, UpdateBalanceRequest.builder()
                    .amount(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP))
                    .transactionType(TransactionType.WAGER)
                    .build()));
        }

        // Execute the tasks concurrently
        List<Future<?>> futures = new ArrayList<>();
        for (Runnable task : tasks) {
            futures.add(executor.submit(task));
        }

        // Wait for the tasks to complete
        for (Future<?> future : futures) {
            future.get();
        }

        // Verify that the player's balance has been updated correctly
        var player = playerRepository.findById(1).orElseThrow();
        assertEquals(new BigDecimal("450.00"), player.getBalance());
    }
}