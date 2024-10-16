package com.example.server.auth.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String email;
    private String password;
}
