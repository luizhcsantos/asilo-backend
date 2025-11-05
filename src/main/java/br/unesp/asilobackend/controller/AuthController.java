package br.unesp.asilobackend.controller;

import br.unesp.asilobackend.dto.LoginRequest;
import br.unesp.asilobackend.dto.LoginResponse;
import br.unesp.asilobackend.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
            // Seu AutenticacaoService parece retornar o token como String
            String token = autenticacaoService.autenticar(loginRequest.username, loginRequest.password);

            // O frontend espera um objeto JSON com a chave "token"
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception e) {
            // Idealmente, trate exceções de autenticação (ex: BadCredentialsException)
            return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas"));
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
