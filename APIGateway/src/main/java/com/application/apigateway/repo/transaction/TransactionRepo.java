package com.application.apigateway.repo.transaction;

import com.application.apigateway.entity.transaction.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionDetails, Long> {
    List<TransactionDetails> findAllByAccountNumberOrderByCreatedDTDesc(String accountNumber);

    @Query("select t.accountNumber as accountNumber, sum(t.transactionAmount) as totalAmount " +
            " from TransactionDetails as t " +
            " group by t.accountNumber ")
    List<TransactionDTO> getDemo();
}
