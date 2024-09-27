package com.application.apigateway.service.user;


import com.application.apigateway.dto.CommonResponse;
import com.application.apigateway.entity.user.User;
import com.application.apigateway.payload.request.auth.LoginRequest;
import com.application.apigateway.payload.request.auth.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(SignUpRequest signupReq);
    CommonResponse loginUser(LoginRequest loginRequest);
}
