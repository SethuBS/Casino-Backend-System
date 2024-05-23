package com.casino.backend.request;

import com.casino.backend.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class UpdateBalanceRequest {
    private BigDecimal amount;
    private TransactionType transactionType;
}
