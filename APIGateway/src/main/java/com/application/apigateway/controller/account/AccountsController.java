package com.application.apigateway.controller.account;

import com.application.apigateway.dto.CommonResponse;
import com.application.apigateway.dto.TransactionDto;
import com.application.apigateway.repo.transaction.TransactionRepo;
import com.application.apigateway.service.account.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountsController {
    private final AccountService accountService;
    private final TransactionRepo transactionRepo;

    @PostMapping("/debit")
    public CommonResponse debitAccount(@RequestBody TransactionDto dto, HttpServletRequest request) {
        return accountService.debitOperation(dto, request);
    }

    @PostMapping("/credit")
    public CommonResponse creditAccount(@RequestBody TransactionDto dto, HttpServletRequest request) {
        return accountService.creditOperation(dto, request);
    }


    @GetMapping("/get-all-transactions")
    public CommonResponse creditAccount(@RequestParam("accountNumber") String accountNumber, HttpServletRequest request) {
        return accountService.getTransactionDetails(accountNumber, request);
    }

    @GetMapping("/get-all-transactions-summary")
    public List<?> transactionSummary() {
        return transactionRepo.getDemo().stream().toList();
    }
}
