package com.application.apigateway.enums.transaction;

import lombok.Getter;

@Getter
public enum TransactionType {
    Debit,
    Credit;

    public static TransactionType getValue(String value) {
        if (value.equals("Debit")) {
            return TransactionType.Debit;
        } else if (value.equals("Credit")) {
            return TransactionType.Credit;
        } else {
            return null;
        }
    }
}
