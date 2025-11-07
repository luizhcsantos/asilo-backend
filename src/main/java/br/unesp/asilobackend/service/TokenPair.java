package br.unesp.asilobackend.service;

public class TokenPair {
    private final String token;
    private final String refreshToken;

    public TokenPair(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
