package br.unesp.asilobackend.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private final String token;
    private final String refreshToken;

    public LoginResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public LoginResponse(String token) {
        this(token, null);
    }
}