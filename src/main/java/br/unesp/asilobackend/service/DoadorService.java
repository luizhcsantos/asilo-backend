package br.unesp.asilobackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.domain.PessoaFisica;
import br.unesp.asilobackend.domain.PessoaJuridica;
import br.unesp.asilobackend.dto.PessoaFisicaRequestDTO;
import br.unesp.asilobackend.dto.PessoaFisicaResponseDTO;
import br.unesp.asilobackend.dto.PessoaJuridicaRequestDTO;
import br.unesp.asilobackend.dto.PessoaJuridicaResponseDTO;
import br.unesp.asilobackend.repository.AdministradorRepository;
import br.unesp.asilobackend.repository.DoadorRepository;

@Service
public class DoadorService {

    @Autowired
    private DoadorRepository doadorRepository;
    @Autowired
    private AdministradorRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método privado para validar senha
    private void validarSenha(String senha) throws Exception {
        if (senha == null || senha.trim().isEmpty()) {
            throw new Exception("A senha não pode ser nula ou vazia.");
        }
    }

    /**
     * Cadastra uma nova Pessoa Física
     */
    public PessoaFisicaResponseDTO salvarPessoaFisica(PessoaFisicaRequestDTO dto) throws Exception {
        validarSenha(dto.getPessoaFisicaDtoSenha());

        // Validar se o email já existe (em doadores OU admins)
        if (doadorRepository.buscarPorEmail(dto.getPessoaFisicaDtoEmail()).isPresent() ||
                adminRepository.buscarPorEmail(dto.getPessoaFisicaDtoEmail()).isPresent()) {
            throw new Exception("Este e-mail já está em uso.");
        }

        // Validar se o CPF já existe
        if (doadorRepository.buscarPorCpf(dto.getPessoaFisicaDtoCpf()).isPresent()) {
            throw new Exception("Este CPF já está cadastrado.");
        }

        PessoaFisica pf = new PessoaFisica();
        // Mapear DTO -> Domínio (nomes de campos atualizados)
        pf.setNome(dto.getPessoaFisicaDtoNome());
        pf.setEmail(dto.getPessoaFisicaDtoEmail());
        pf.setTelefone(dto.getPessoaFisicaDtoTelefone());
        pf.setCpf(dto.getPessoaFisicaDtoCpf());

        // Codifica a senha antes de salvar (armazenada em senhaHash no domínio)
        pf.setSenhaHash(passwordEncoder.encode(dto.getPessoaFisicaDtoSenha()));

        // Salva no arquivo usando o repositório
        // doadorSalvo = doadorRepository.save(pf); 
        PessoaFisica salvo = (PessoaFisica) doadorRepository.save(pf);

        // Mapear domínio -> DTO de resposta
        PessoaFisicaResponseDTO out = new PessoaFisicaResponseDTO();
        out.setPessoaFisicaDtoNome(salvo.getNome());
        out.setPessoaFisicaDtoCpf(salvo.getCpf());
        out.setPessoaFisicaDtoEmail(salvo.getEmail());
        out.setPessoaFisicaDtoTelefone(salvo.getTelefone());
        // Não retornamos a senha no DTO de resposta por razões de segurança
        return out;
        // return new PessoaFisicaResponseDTO((PessoaFisica) doadorSalvo);
    }

    /**
     * Cadastra uma nova Pessoa Jurídica
     */
    public PessoaJuridicaResponseDTO salvarPessoaJuridica(PessoaJuridicaRequestDTO dto) throws Exception {
        validarSenha(dto.getPessoaJuridicaDtoSenha());

        // Validar se o email já existe (em doadores OU admins)
        if (doadorRepository.buscarPorEmail(dto.getPessoaJuridicaDtoEmail()).isPresent() ||
                adminRepository.buscarPorEmail(dto.getPessoaJuridicaDtoEmail()).isPresent()) {
            throw new Exception("Este e-mail já está em uso.");
        }

        // Validar se o CNPJ já existe
        if (doadorRepository.buscarPorCnpj(dto.getPessoaJuridicaDtoCnpj()).isPresent()) {
            throw new Exception("Este CNPJ já está cadastrado.");
        }

        PessoaJuridica pj = new PessoaJuridica();
        pj.setNome(dto.getPessoaJuridicaDtoNomeFantasia()); // Nome social
        pj.setEmail(dto.getPessoaJuridicaDtoEmail());
        pj.setTelefone(dto.getPessoaJuridicaDtoTelefone());
        pj.setCnpj(dto.getPessoaJuridicaDtoCnpj());

        pj.setSenhaHash(passwordEncoder.encode(dto.getPessoaJuridicaDtoSenha()));

        PessoaJuridica salvo = (PessoaJuridica) doadorRepository.save(pj);

        PessoaJuridicaResponseDTO out = new PessoaJuridicaResponseDTO();
        out.setPessoaJuridicaDtoNomeFantasia(salvo.getNome());
        out.setPessoaJuridicaDtoCnpj(salvo.getCnpj());
        out.setPessoaJuridicaDtoEmail(salvo.getEmail());
        out.setPessoaJuridicaDtoTelefone(salvo.getTelefone());
        // Não retornamos a senha no DTO de resposta por razões de segurança
        return out;
    }

    public Doador buscarPorId(Long id) throws Exception {
        return doadorRepository.buscarPorId(id)
                .orElseThrow(() -> new Exception("Doador não encontrado com a ID "+ id));
    }
}
