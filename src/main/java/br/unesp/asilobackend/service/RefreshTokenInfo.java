package br.unesp.asilobackend.service;

import java.time.Instant;

public class RefreshTokenInfo {
    private final String username;
    private final Instant expiresAt;

    public RefreshTokenInfo(String username, Instant expiresAt) {
        this.username = username;
        this.expiresAt = expiresAt;
    }

    public String getUsername() {
        return username;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }
}
