package com.application.apigateway.service.account;


import com.application.apigateway.config.exception.ClientSideException;
import com.application.apigateway.config.security.jwt.JwtUtilsService;
import com.application.apigateway.dto.CommonResponse;
import com.application.apigateway.dto.TransactionDto;
import com.application.apigateway.entity.CommonAttributes;
import com.application.apigateway.entity.account.AccountDetails;
import com.application.apigateway.entity.transaction.TransactionDetails;
import com.application.apigateway.entity.user.User;
import com.application.apigateway.enums.transaction.TransactionType;
import com.application.apigateway.repo.account.AccountRepo;
import com.application.apigateway.repo.transaction.TransactionRepo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;
    private final JwtUtilsService jwtUtilsService;
    @Autowired
    @Lazy
    AccountService accountService;
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public void createAccount(User user) {
        try {
            AccountDetails accountDetails = new AccountDetails();
            accountDetails.setAccountHolder(user);
            accountDetails.setBalance(0.0);
            accountDetails.setAccountNumber(getAccountNumber(user));
            accountRepo.save(accountDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public CommonResponse debitOperation(TransactionDto dto, HttpServletRequest request) {
        List<AccountDetails> list = new ArrayList<>();

        if (!ObjectUtils.isEmpty(dto.getAccountNumberTo()) && !ObjectUtils.isEmpty(dto.getAccountNumberFrom())) {
            Optional<AccountDetails> toAccountDetails = accountRepo.findByAccountNumber(dto.getAccountNumberTo());
            Optional<AccountDetails> fromAccountDetails = accountRepo.findByAccountNumber(dto.getAccountNumberFrom());

            if (toAccountDetails.isPresent() && fromAccountDetails.isPresent()) {
                if (fromAccountDetails.get().getBalance() >= dto.getAmount()) {
                    toAccountDetails.get().setBalance(toAccountDetails.get().getBalance() + dto.getAmount());
                    fromAccountDetails.get().setBalance(fromAccountDetails.get().getBalance() - dto.getAmount());
                    onUpdateOperation(toAccountDetails.get(), request);
                    onUpdateOperation(fromAccountDetails.get(), request);
                    list.add(fromAccountDetails.get());
                    list.add(toAccountDetails.get());
                    accountRepo.saveAll(list);
                    List<TransactionDetails> txList = accountService.saveTransaction(dto, fromAccountDetails.get(), toAccountDetails.get(), request);
                    return new CommonResponse(200, true, "Successfully Debited", txList);
                } else {
                    throw new ClientSideException("Insufficient Balance");
                }
            } else {
                throw new ClientSideException("Invalid Accounts");
            }
        } else {
            throw new ClientSideException("Invalid Accounts");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.REPEATABLE_READ)
    public CommonResponse creditOperation(TransactionDto dto, HttpServletRequest request) {
        if (!ObjectUtils.isEmpty(dto.getAccountNumberTo())) {
            Optional<AccountDetails> accountDetails = accountRepo.findByAccountNumber(dto.getAccountNumberTo());
            if (accountDetails.isPresent()) {
                accountDetails.get().setBalance(accountDetails.get().getBalance() + dto.getAmount());
                onUpdateOperation(accountDetails.get(), request);
                accountRepo.save(accountDetails.get());
                List<TransactionDetails> txList = accountService.saveTransaction(dto, accountDetails.get(), null, request);
                return new CommonResponse(200, true, "Successfully Credited", txList);
            } else {
                throw new ClientSideException("Account Not Found.");
            }
        } else {
            throw new ClientSideException("Account Number Is Empty.");
        }
    }


    public List<TransactionDetails> saveTransaction(TransactionDto dto, AccountDetails fromAccountDetails,
                                                    AccountDetails toAccountDetails, HttpServletRequest request) {
        List<TransactionDetails> list = new ArrayList<>();


        TransactionDetails fromTransactionDetails = new TransactionDetails();
        fromTransactionDetails.setTransactionNumber(getTransactionNumber());
        fromTransactionDetails.setTransactionAmount(dto.getAmount());
        fromTransactionDetails.setTransactionFromAccount(fromAccountDetails);
        fromTransactionDetails.setTransactionType(TransactionType.getValue(dto.getTransactionType()));
        fromTransactionDetails.setAccountNumber(dto.getAccountNumberTo());
        onCreateOperation(fromTransactionDetails, request);

        if (!ObjectUtils.isEmpty(toAccountDetails) && TransactionType.Debit.toString().equals(dto.getTransactionType())) {
            fromTransactionDetails.setTransactionToAccount(toAccountDetails);

            TransactionDetails toTransactionDetails = new TransactionDetails();
            modelMapper.map(fromTransactionDetails, toTransactionDetails);
            toTransactionDetails.setTransactionType(TransactionType.Credit);
            toTransactionDetails.setAccountNumber(toAccountDetails.getAccountNumber());
            list.add(toTransactionDetails);
        }

        list.add(fromTransactionDetails);


        list = transactionRepo.saveAll(list);
        return list;
    }

    public CommonResponse getTransactionDetails(String accountNumber, HttpServletRequest request) {
        if (!ObjectUtils.isEmpty(accountNumber)) {
            Optional<AccountDetails> accountDetails = accountRepo.findByAccountNumber(accountNumber);
            if (accountDetails.isPresent()) {
                List<TransactionDetails> list = transactionRepo.findAllByAccountNumberOrderByCreatedDTDesc(accountNumber);
                return new CommonResponse(200, true, "Successfully Retrieved", list);
            } else {
                throw new ClientSideException("Account Number Not Found");
            }
        } else {
            throw new ClientSideException("Account Number Is Empty.");
        }
    }

    private String getTransactionNumber() {
        String txNum = "T";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        txNum = txNum + LocalDateTime.now().format(formatter);
        return txNum;
    }

    private String getAccountNumber(User user) {
        String accountNumber = "A-0000000000";
        String userId = user.getId().toString();
        int userIdLen = userId.length(), fullLen = accountNumber.length();
        accountNumber = new String(accountNumber.substring(0, fullLen - userIdLen) + userId);
        return accountNumber;
    }

    public void onCreateOperation(CommonAttributes attributes, HttpServletRequest request) {
        User user = jwtUtilsService.getUserFromToken(request);
        attributes.setCreatedBy(user.getId());
        attributes.setCreatedByUsername(user.getUsername());
    }

    public void onUpdateOperation(CommonAttributes attributes, HttpServletRequest request) {
        User user = jwtUtilsService.getUserFromToken(request);
        attributes.setUpdatedBy(user.getId());
        attributes.setUpdatedByUsername(user.getUsername());
    }
}
