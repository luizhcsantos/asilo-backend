package br.unesp.asilobackend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.unesp.asilobackend.domain.Administrador;
import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.dto.AdminDTO;
import br.unesp.asilobackend.dto.DoadorListItemDTO;
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

        // Persistir
        adminRepository.salvar(admin);

        // Criar DTO de resposta sem a senha
        AdminDTO resposta = new AdminDTO();
        resposta.setAdminDtoNome(admin.getNome());
        resposta.setAdminDtoEmail(admin.getEmail());

        return resposta;
    }

    public List<DoadorListItemDTO> listarDoadoresListItem() {
        return doadorRepository.buscarTodos().stream()
                .map(DoadorListItemDTO::new)
                .toList();
    }

    public AdminDTO obterPerfil(String email) throws Exception {
        Optional<Administrador> opt = adminRepository.buscarPorEmail(email);
        if (opt.isEmpty()) throw new Exception("Administrador não encontrado");
        return new AdminDTO(opt.get());
    }

    public AdminDTO atualizarPerfil(String emailAtual,
                                    String novoNome,
                                    String novoEmail,
                                    String senhaAtual,
                                    String novaSenha) throws Exception {
        Optional<Administrador> opt = adminRepository.buscarPorEmail(emailAtual);
        if (opt.isEmpty()) throw new Exception("Administrador não encontrado");
        Administrador admin = opt.get();

        if (novoEmail != null && !novoEmail.equalsIgnoreCase(emailAtual)) {
            // verificar conflitos de e-mail
            if (adminRepository.buscarPorEmail(novoEmail).isPresent()) {
                throw new Exception("E-mail já utilizado por outro administrador");
            }
            if (doadorRepository.buscarPorEmail(novoEmail).isPresent()) {
                throw new Exception("E-mail já utilizado por um doador");
            }
            admin.setEmail(novoEmail);
        }

        if (novoNome != null && !novoNome.isBlank()) {
            admin.setNome(novoNome);
        }

        if (novaSenha != null && !novaSenha.isBlank()) {
            if (senhaAtual == null || !passwordEncoder.matches(senhaAtual, admin.getSenhaHash())) {
                throw new Exception("Senha atual inválida");
            }
            admin.setSenhaHash(passwordEncoder.encode(novaSenha));
        }

        adminRepository.salvar(admin);
        return new AdminDTO(admin);
    }
}
