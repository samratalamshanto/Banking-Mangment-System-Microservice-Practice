package com.application.apigateway.controller.auth;


import com.application.apigateway.config.exception.TooManyReqException;
import com.application.apigateway.config.ratelimiter.RateLimiterService;
import com.application.apigateway.dto.CommonResponse;
import com.application.apigateway.payload.request.auth.LoginRequest;
import com.application.apigateway.payload.request.auth.SignUpRequest;
import com.application.apigateway.service.auth.AuthService;
import com.application.apigateway.service.user.UserService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;
    private final RateLimiterService rateLimiterService;

    @PostMapping("/signup")
    public CommonResponse signUp(@RequestBody final SignUpRequest signUpRequest, HttpServletRequest request) {
        Bucket bucket = rateLimiterService.getBucket(request.getRemoteAddr());
        if (!bucket.tryConsume(1)) {
            throw new TooManyReqException("Too many requests");
        }

        return authService.registerUser(signUpRequest);
    }

    @PostMapping("/login")
    public CommonResponse logIn(@RequestBody final LoginRequest loginRequest, HttpServletRequest request) {
        Bucket bucket = rateLimiterService.getBucket(request.getRemoteAddr());
        if (!bucket.tryConsume(1)) {
            throw new TooManyReqException("Too many requests");
        }
        return userService.loginUser(loginRequest);
    }

    @GetMapping("/rate-limiter-test")
    public CommonResponse getObjTest(HttpServletRequest request) {
        Bucket bucket = rateLimiterService.getBucket(request.getRemoteAddr());
        if (!bucket.tryConsume(1)) {
            throw new TooManyReqException("Too many requests");
        }
        return new CommonResponse();
    }

}
