package com.application.apigateway.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    @NotNull(message = "Transaction Type can not be null or empty.")
    private String transactionType;
    @NotNull(message = "Transaction amount can not be null or empty.")
    private double amount;
    private String accountNumberFrom;
    @NotNull(message = "Account Number can not be null or empty.")
    private String accountNumberTo;
}
