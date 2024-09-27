package com.application.apigateway.service.auth;


import com.application.apigateway.dto.CommonResponse;
import com.application.apigateway.entity.user.User;
import com.application.apigateway.payload.request.auth.SignUpRequest;
import com.application.apigateway.service.account.AccountService;
import com.application.apigateway.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final AccountService accountService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public CommonResponse registerUser(SignUpRequest signupReq) {
        User user = userService.createUser(signupReq);
        accountService.createAccount(user);
        CommonResponse commonResponse = new CommonResponse(200, true, "Successfully Registered.", user);
        return commonResponse;
    }
}
