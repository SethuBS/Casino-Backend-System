package com.casino.backend.repository;

import com.casino.backend.entity.Player;
import com.casino.backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findTop10ByPlayerOrderByTimestampDesc(Player player);
}
