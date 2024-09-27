package com.application.apigateway.payload.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
}
