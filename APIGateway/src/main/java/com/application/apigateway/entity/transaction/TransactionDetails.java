package com.application.apigateway.entity.transaction;

import com.application.apigateway.entity.CommonAttributes;
import com.application.apigateway.entity.account.AccountDetails;
import com.application.apigateway.enums.transaction.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class TransactionDetails extends CommonAttributes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionNumber;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private double transactionAmount;
    private String accountNumber;
    @ManyToOne
    private AccountDetails transactionFromAccount;
    @ManyToOne
    private AccountDetails transactionToAccount;

}
