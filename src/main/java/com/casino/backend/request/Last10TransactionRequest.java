package com.casino.backend.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Last10TransactionRequest {
    private String username;
}
