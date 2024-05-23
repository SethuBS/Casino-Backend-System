package com.casino.backend.controller;


import com.casino.backend.request.Last10TransactionRequest;
import com.casino.backend.request.UpdateBalanceRequest;
import com.casino.backend.response.BalanceResponse;
import com.casino.backend.response.Last10TransactionResponse;
import com.casino.backend.response.UpdateBalanceResponse;
import com.casino.backend.service.PlayerService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Log4j2
@CrossOrigin("*")
@RestController
@RequestMapping("/casino")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/player/{playerId}/balance")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Integer playerId) {
        var balance = playerService.getBalance(playerId);
        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @PostMapping("/player/{playerId}/balance/update")
    public ResponseEntity<UpdateBalanceResponse> updateBalance(@PathVariable Integer playerId,
                                                               @RequestBody UpdateBalanceRequest request) {
        var updatedBalance = playerService.updateBalance(playerId, request);
        return new ResponseEntity<>(updatedBalance, HttpStatus.OK);
    }

    @PostMapping("/admin/player/transactions")
    public ResponseEntity<List<Last10TransactionResponse>> getLast10Transactions(@RequestBody Last10TransactionRequest request) {
        var player = playerService.getPlayerByUsername(request.getUsername());
        List<Last10TransactionResponse> transactions = playerService.getLast10Transactions(player);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }
}
