package com.application.apigateway.controller.auth;


import com.application.apigateway.dto.CommonResponse;
import com.application.apigateway.payload.request.auth.LoginRequest;
import com.application.apigateway.payload.request.auth.SignUpRequest;
import com.application.apigateway.service.auth.AuthService;
import com.application.apigateway.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public CommonResponse signUp(@RequestBody final SignUpRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @PostMapping("/login")
    public CommonResponse logIn(@RequestBody final LoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }

}
