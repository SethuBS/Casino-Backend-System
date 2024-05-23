package com.casino.backend.controller;


import com.casino.backend.entity.Player;
import com.casino.backend.enums.TransactionType;
import com.casino.backend.exception.InsufficientBalanceException;
import com.casino.backend.exception.InvalidTransactionException;
import com.casino.backend.exception.PlayerNotFoundException;
import com.casino.backend.request.Last10TransactionRequest;
import com.casino.backend.request.UpdateBalanceRequest;
import com.casino.backend.response.BalanceResponse;
import com.casino.backend.response.Last10TransactionResponse;
import com.casino.backend.response.UpdateBalanceResponse;
import com.casino.backend.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.aspectj.bridge.MessageUtil.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;


    @Test
    void testGetPlayerBalance() {
        // Given
        var player = new Player();
        player.setPlayerId(1);
        player.setUsername("testUsername");
        player.setBalance(BigDecimal.valueOf(100));

        var playerBalanceResponse = BalanceResponse
                .builder().balance(player.getBalance()).playerId(player.getPlayerId())
                .build();
        when(playerService.getBalance(any())).thenReturn(playerBalanceResponse);

        // When
        ResponseEntity<BalanceResponse> response = playerController.getBalance(player.getPlayerId());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(player.getPlayerId(), Objects.requireNonNull(response.getBody()).getPlayerId());
        assertEquals(BigDecimal.valueOf(100), response.getBody().getBalance());
    }

    @Test
    void testUpdatePlayerBalance_Wager() {
        // Given
        var playerId = 1;
        var transactionId = 1;
        var request = new UpdateBalanceRequest(BigDecimal.valueOf(20), TransactionType.WAGER);
        when(playerService.updateBalance(any(), any())).thenReturn(new UpdateBalanceResponse(transactionId, BigDecimal.valueOf(80)));

        // When
        ResponseEntity<UpdateBalanceResponse> response = playerController.updateBalance(playerId, request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new UpdateBalanceResponse(transactionId, BigDecimal.valueOf(80)).getTransactionId(),
                Objects.requireNonNull(response.getBody()).getTransactionId());
        assertEquals(new UpdateBalanceResponse(transactionId, BigDecimal.valueOf(80)).getBalance(), response.getBody().getBalance());
    }

    @Test
    void testUpdatePlayerBalance_Win() {
        // Given
        var transactionId = 1;
        var playerId = 1;
        var request = new UpdateBalanceRequest(BigDecimal.valueOf(20), TransactionType.WIN);
        when(playerService.updateBalance(any(), any())).thenReturn(new UpdateBalanceResponse(transactionId, BigDecimal.valueOf(120)));

        // When
        ResponseEntity<UpdateBalanceResponse> response = playerController.updateBalance(playerId, request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new UpdateBalanceResponse(transactionId, BigDecimal.valueOf(120)).getTransactionId(),
                Objects.requireNonNull(response.getBody()).getTransactionId());
        assertEquals(new UpdateBalanceResponse(transactionId, BigDecimal.valueOf(120)).getBalance(), response.getBody().getBalance());
    }

    @Test
    void testUpdatePlayerBalance_InsufficientBalance() {
        // Given
        var playerId = 1;
        UpdateBalanceRequest request = new UpdateBalanceRequest(BigDecimal.valueOf(150), TransactionType.WAGER);
        when(playerService.updateBalance(any(), any())).thenThrow(new InsufficientBalanceException("Wager greater than current balance"));

        // When
        try {
            playerController.updateBalance(playerId, request);
            fail("Expected InsufficientBalanceException");
        } catch (InsufficientBalanceException e) {
            assertEquals("Wager greater than current balance", e.getMessage());
        }
    }

    @Test
    void testUpdatePlayerBalance_InvalidTransaction() {
        // Given
        var playerId = 1;
        UpdateBalanceRequest request = new UpdateBalanceRequest(BigDecimal.valueOf(-10), TransactionType.WAGER);

        // When
        when(playerService.updateBalance(any(), any())).thenThrow(new InvalidTransactionException("Amount must be positive"));

        // Then
        assertThrows(InvalidTransactionException.class, () -> playerController.updateBalance(playerId, request));
    }

    @Test
    void testUpdatePlayerBalance_PlayerNotFound() {
        // Given
        var playerId = 100;
        UpdateBalanceRequest request = new UpdateBalanceRequest(BigDecimal.valueOf(10), TransactionType.WAGER);

        // When
        when(playerService.updateBalance(any(), any())).thenThrow(new PlayerNotFoundException("Invalid playerId"));


        // Then
        assertThrows(PlayerNotFoundException.class, () -> playerController.updateBalance(playerId, request));
    }

    @Test
    public void testGetLast10Transactions_ValidRequest() {
        // Given
        var request = new Last10TransactionRequest("player1");
        var player = new Player(1, "player1", BigDecimal.valueOf(1000));
        List<Last10TransactionResponse> transactions = new ArrayList<>();
        when(playerService.getPlayerByUsername(request.getUsername())).thenReturn(player);
        when(playerService.getLast10Transactions(player)).thenReturn(transactions);

        // When
        ResponseEntity<List<Last10TransactionResponse>> response = playerController.getLast10Transactions(request);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transactions, response.getBody());
    }

    @Test
    public void testGetLast10Transactions_InvalidRequest() {
        // Given
        var request = new Last10TransactionRequest("player1");

        // When
        when(playerService.getPlayerByUsername(request.getUsername())).thenThrow(new PlayerNotFoundException("Invalid username"));

        // Then
        assertThrows(PlayerNotFoundException.class, () -> playerController.getLast10Transactions(request));
    }
}
