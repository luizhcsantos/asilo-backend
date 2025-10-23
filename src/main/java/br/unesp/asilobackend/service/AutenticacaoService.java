package br.unesp.asilobackend.service;

import br.unesp.asilobackend.domain.Administrador;
import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.domain.enums.TipoDoador; // Mantido se 'login' antigo for usado
import br.unesp.asilobackend.repository.AdministradorRepository;
import br.unesp.asilobackend.repository.DoadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService {

    @Autowired
    private DoadorRepository doadorRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // IMPLEMENTAÇÃO DO MÉTODO 'autenticar'
    public String autenticar(String username, String password) throws Exception {
        // 1. Tenta encontrar como Doador
        Optional<Doador> doadorOpt = doadorRepository.buscarPorEmail(username);
        if (doadorOpt.isPresent()) {
            Doador doador = doadorOpt.get();
            // 1a. Verifica se a senha bate
            if (passwordEncoder.matches(password, doador.getDoadorSenha())) {
                // Sucesso! Retorna um "token" simples.
                return "token-doador-" + doador.getDoadorId();
            }
        }

        // 2. Tenta encontrar como Administrador
        Optional<Administrador> adminOpt = administradorRepository.buscarPorEmail(username);
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            // 2a. Verifica se a senha bate
            if (passwordEncoder.matches(password, admin.getAdminSenha())) {
                // Sucesso! Retorna um "token" simples.
                return "token-admin-" + admin.getAdminId();
            }
        }

        // 3. Se não encontrou ou a senha falhou
        throw new Exception("Credenciais inválidas");
    }

    // Métodos antigos
    public boolean login(TipoDoador tipo, String email, String senha) {
        return false;
    }

    public void recuperarSenha(String email) {
        // Lógica de recuperação de senha (futura)
    }
}