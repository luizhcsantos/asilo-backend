package br.unesp.asilobackend.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.unesp.asilobackend.domain.Administrador;
import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.repository.AdministradorRepository;
import br.unesp.asilobackend.repository.DoadorRepository;

@Service
public class AutenticacaoService {

    @Autowired
    private DoadorRepository doadorRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenStore refreshTokenStore;

    @Value("${jwt.refresh-expiration-days}")
    private int refreshExpirationDays;

    // IMPLEMENTAÇÃO DO MÉTODO 'autenticar'
    public String autenticar(String username, String password) throws Exception {
        // 1. Tenta encontrar como Doador
        Optional<Doador> doadorOpt = doadorRepository.buscarPorEmail(username);
        if (doadorOpt.isPresent()) {
            Doador doador = doadorOpt.get();
            // 1a. Verifica se a senha bate
            if (passwordEncoder.matches(password, doador.getSenhaHash())) {
                // Sucesso! Retorna um "token" simples.
                return jwtService.generateAccessToken(doador.getEmail(), "ROLE_DOADOR");
            }
        }

        // 2. Tenta encontrar como Administrador
        Optional<Administrador> adminOpt = administradorRepository.buscarPorEmail(username);
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            // 2a. Verifica se a senha bate
            if (passwordEncoder.matches(password, admin.getSenhaHash())) {
                // Sucesso! Retorna um "token" simples.
                return jwtService.generateAccessToken(admin.getEmail(), "ROLE_ADMIN");
            }
        }

        // 3. Se não encontrou ou a senha falhou
        throw new Exception("Credenciais inválidas");
    }

    public boolean solicitarRecuperacaoSenha(String email) {
        return false;
    }

    private String authenticateAndReturnUsername(String username, String password) throws Exception {
        // 1. Tenta encontrar como Doador
        Optional<Doador> doadorOpt = doadorRepository.buscarPorEmail(username);
        if (doadorOpt.isPresent()) {
            Doador doador = doadorOpt.get();
            // 1a. Verifica se a senha bate
            if (passwordEncoder.matches(password, doador.getSenhaHash())) {
                // Sucesso! Retornamos o identificador/username (usar email para assunto)
                return doador.getEmail();
            }
        }

        // 2. Tenta encontrar como Administrador
        Optional<Administrador> adminOpt = administradorRepository.buscarPorEmail(username);
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            // 2a. Verifica se a senha bate
            if (passwordEncoder.matches(password, admin.getSenhaHash())) {
                // Sucesso! Retornamos o identificador/username (usar email)
                return admin.getEmail();
            }
        }

        // 3. Se não encontrou ou a senha falhou
        throw new Exception("Credenciais inválidas");
    }

    private String determineRole(String username) {
        if (administradorRepository.buscarPorEmail(username).isPresent()) {
            return "ROLE_ADMIN";
        }
        return "ROLE_DOADOR";
    }

    public TokenPair login(String username, String password) throws Exception {
        String subject = authenticateAndReturnUsername(username, password);
        String role = determineRole(subject);
        String access = jwtService.generateAccessToken(subject, role);
        String refresh = refreshTokenStore.createForUsername(subject);
        return new TokenPair(access, refresh);
    }

    public TokenPair refresh(String refreshToken) throws Exception {
        String username = refreshTokenStore.getUsernameIfValid(refreshToken);
        if (username == null) throw new Exception("refresh token inválido ou expirado");

        // Rotate: revoke old, create new
        refreshTokenStore.revoke(refreshToken);
        String newRefresh = refreshTokenStore.createForUsername(username);
        String role = determineRole(username);
        String newAccess = jwtService.generateAccessToken(username, role);
        return new TokenPair(newAccess, newRefresh);
    }
}