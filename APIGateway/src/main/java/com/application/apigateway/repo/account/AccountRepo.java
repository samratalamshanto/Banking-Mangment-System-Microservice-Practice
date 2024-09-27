package com.application.apigateway.repo.account;


import com.application.apigateway.entity.account.AccountDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<AccountDetails, Long> {
    Optional<AccountDetails> findByAccountNumber(String accountNumber);
}
