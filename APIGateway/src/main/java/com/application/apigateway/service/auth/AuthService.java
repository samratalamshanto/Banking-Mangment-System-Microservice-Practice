package com.application.apigateway.service.auth;


import com.application.apigateway.dto.CommonResponse;
import com.application.apigateway.payload.request.auth.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    CommonResponse registerUser(SignUpRequest signupReq);
}
