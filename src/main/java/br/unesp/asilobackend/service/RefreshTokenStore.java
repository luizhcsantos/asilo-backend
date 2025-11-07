package br.unesp.asilobackend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenStore {

    // Simple in-memory store: token -> RefreshTokenInfo
    private final Map<String, RefreshTokenInfo> store = new ConcurrentHashMap<>();

    @Value("${jwt.refresh-expiration-days}")
    private int refreshExpirationDays;

    public String createForUsername(String username) {
        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(refreshExpirationDays, ChronoUnit.DAYS);
        store.put(token, new RefreshTokenInfo(username, expiresAt));
        return token;
    }

    public String getUsernameIfValid(String token) {
        RefreshTokenInfo info = store.get(token);
        if (info == null) return null;
        if (info.isExpired()) {
            store.remove(token);
            return null;
        }
        return info.getUsername();
    }

    public void revoke(String token) {
        store.remove(token);
    }
}
