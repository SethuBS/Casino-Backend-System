package com.casino.backend.response;

import com.casino.backend.enums.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class Last10TransactionResponse {
    private TransactionType transactionType;
    private Integer transactionId;
    private BigDecimal amount;
}
