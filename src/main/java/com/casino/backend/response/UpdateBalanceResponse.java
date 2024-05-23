package com.casino.backend.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@Builder
public class UpdateBalanceResponse {
    private Integer transactionId;
    private BigDecimal balance;
}
