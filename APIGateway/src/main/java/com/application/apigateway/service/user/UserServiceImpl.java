package com.application.apigateway.service.user;

import com.application.apigateway.config.exception.UserAlreadyExistsException;
import com.application.apigateway.config.security.jwt.JwtUtilsService;
import com.application.apigateway.dto.CommonResponse;
import com.application.apigateway.entity.user.User;
import com.application.apigateway.enums.CommonEnum;
import com.application.apigateway.enums.roles.RoleEnum;
import com.application.apigateway.payload.request.auth.LoginRequest;
import com.application.apigateway.payload.request.auth.SignUpRequest;
import com.application.apigateway.payload.response.auth.LoginResponse;
import com.application.apigateway.repo.user.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepository;
    private final JwtUtilsService jwtUtilsService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User createUser(SignUpRequest signupReq) {
        Optional<User> existingUser = userRepository.findByUsername(signupReq.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("This username already exists");
        }

        try {
            User user = new User();
            user.setName(signupReq.getName());
            user.setPhone(signupReq.getPhone());
            user.setEmail(signupReq.getEmail());
            user.setUsername(signupReq.getUsername());
            user.setPassword(passwordEncoder.encode(signupReq.getPassword()));
            user.setRoles(RoleEnum.User);
            user.setStatus(CommonEnum.Active);
            user = userRepository.save(user);
            return user;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CommonResponse loginUser(LoginRequest loginRequest) {
        User user = authenticate(loginRequest);
        LoginResponse loginResp = new LoginResponse();

        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", user.getUsername());
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());

        loginResp.setToken(jwtUtilsService.generateToken(claims, user));
        loginResp.setRefreshToken(jwtUtilsService.generateRefreshToken(claims, user));
        loginResp.setLoggedInTime(LocalDateTime.now());

        CommonResponse commonResponse = new CommonResponse(200, true, "Successfully logged in", loginResp);
        return commonResponse;
    }

    private User authenticate(LoginRequest loginReq) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReq.getUsername(),
                        loginReq.getPassword()
                )
        );

        return userRepository.findByUsername(loginReq.getUsername())
                .orElseThrow();
    }
}

