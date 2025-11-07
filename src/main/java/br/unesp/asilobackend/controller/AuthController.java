package br.unesp.asilobackend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unesp.asilobackend.dto.LoginRequest;
import br.unesp.asilobackend.dto.LoginResponse;
import br.unesp.asilobackend.service.AutenticacaoService;
import br.unesp.asilobackend.service.TokenPair;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AutenticacaoService autenticacaoService;

    public AuthController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            TokenPair tokenPair = autenticacaoService.login(loginRequest.username, loginRequest.password);
            return ResponseEntity.ok(new LoginResponse(tokenPair.getToken(), tokenPair.getRefreshToken()));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {
        try {
            String refreshToken = payload.get("refreshToken");
            if (refreshToken == null) {
                return ResponseEntity.status(400).body(Map.of("error", "refreshToken é obrigatório"));
            }

            TokenPair tokenPair = autenticacaoService.refresh(refreshToken);
            return ResponseEntity.ok(Map.of("token", tokenPair.getToken(), "refreshToken", tokenPair.getRefreshToken()));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 1. Solicitar recuperação de senha
     * (Conforme diagrama: +solicitarRecuperacaoSenha(email): boolean)
     *
     */
    @PostMapping("/recuperar-senha")
    public ResponseEntity<?> solicitarRecuperacaoSenha(@RequestBody Map<String, String> payload) {
        // Espera um JSON {"email": "..."}
        try {
            String email = payload.get("email");
            boolean sucesso = autenticacaoService.solicitarRecuperacaoSenha(email);
            return ResponseEntity.ok(Map.of("success", sucesso));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
