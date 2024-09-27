package com.application.apigateway.service.account;


import com.application.apigateway.dto.CommonResponse;
import com.application.apigateway.dto.TransactionDto;
import com.application.apigateway.entity.account.AccountDetails;
import com.application.apigateway.entity.transaction.TransactionDetails;
import com.application.apigateway.entity.user.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    void createAccount(User user);

    CommonResponse debitOperation(TransactionDto dto, HttpServletRequest request);

    CommonResponse creditOperation(TransactionDto dto, HttpServletRequest request);

    List<TransactionDetails> saveTransaction(TransactionDto dto, AccountDetails fromAccountDetails, AccountDetails toAccountDetails, HttpServletRequest request);

    CommonResponse getTransactionDetails(String accountNumber, HttpServletRequest request);
}
