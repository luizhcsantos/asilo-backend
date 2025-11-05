package br.unesp.asilobackend.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.unesp.asilobackend.domain.Administrador;
import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.dto.AdminDTO;
import br.unesp.asilobackend.repository.AdministradorRepository;
import br.unesp.asilobackend.repository.DoadorRepository;

@Service
public class AdminService {

    @Autowired
    private DoadorRepository doadorRepository;
    
    @Autowired
    private AdministradorRepository adminRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Lista todos os doadores cadastrados.
     */
    public List<Doador> listarDoadores() {
        return doadorRepository.buscarTodos();
    }

    /**
     * Cadastra um novo administrador
     */
    public AdminDTO cadastrarAdministrador(AdminDTO adminDTO) throws Exception {
        // Validar se o email já existe
        if (adminRepository.buscarPorEmail(adminDTO.getAdminDtoEmail()).isPresent() ||
            doadorRepository.buscarPorEmail(adminDTO.getAdminDtoEmail()).isPresent()) {
            throw new Exception("Este e-mail já está em uso.");
        }

        Administrador admin = new Administrador();
        admin.setNome(adminDTO.getAdminDtoNome());
        admin.setEmail(adminDTO.getAdminDtoEmail());
        admin.setSenhaHash(passwordEncoder.encode(adminDTO.getAdminDtoSenha()));

        // Salvar e criar DTO de resposta sem a senha
        AdminDTO resposta = new AdminDTO();
        resposta.setAdminDtoNome(admin.getNome());
        resposta.setAdminDtoEmail(admin.getEmail());

        return resposta;
    }
}
