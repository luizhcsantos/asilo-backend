package br.unesp.asilobackend.service;

import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.domain.PessoaFisica;
import br.unesp.asilobackend.domain.PessoaJuridica;
import br.unesp.asilobackend.domain.enums.TipoDoador;
import br.unesp.asilobackend.dto.PessoaFisicaDTO;
import br.unesp.asilobackend.dto.PessoaJuridicaDTO;
import br.unesp.asilobackend.repository.AdministradorRepository;
import br.unesp.asilobackend.repository.DoadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public PessoaFisica salvarPessoaFisica(PessoaFisicaDTO dto) throws Exception {
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
        pf.setPessoaFisicaNome(dto.getPessoaFisicaDtoNome());
        pf.setDoadorEmail(dto.getPessoaFisicaDtoEmail());
        pf.setDoadorTelefone(dto.getPessoaFisicaDtoTelefone());
        pf.setDoadorTipo(TipoDoador.FISICO); // Define o tipo
        pf.setPessoaFisicaCpf(dto.getPessoaFisicaDtoCpf());

        // Codifica a senha antes de salvar
        pf.setDoadorSenha(passwordEncoder.encode(dto.getPessoaFisicaDtoSenha()));

        // Salva no arquivo usando o novo repositório
        return (PessoaFisica) doadorRepository.save(pf);
    }

    /**
     * Cadastra uma nova Pessoa Jurídica
     */
    public PessoaJuridica salvarPessoaJuridica(PessoaJuridicaDTO dto) throws Exception {
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
        pj.setPessoaJuridicaNome(dto.getPessoaJuridicaDtoNomeFantasia()); // No DTO de PJ, 'nome' é a Razão Social
        pj.setDoadorEmail(dto.getPessoaJuridicaDtoEmail());
        pj.setDoadorTelefone(dto.getPessoaJuridicaDtoTelefone());
        pj.setDoadorTipo(TipoDoador.JURIDICO); // Define o tipo
        pj.setPessoaJuridicaCnpj(dto.getPessoaJuridicaDtoCnpj());

        // Codifica a senha antes de salvar
        pj.setDoadorSenha(passwordEncoder.encode(dto.getPessoaJuridicaDtoSenha()));

        // Salva no arquivo usando o novo repositório
        return (PessoaJuridica) doadorRepository.save(pj);
    }

    public Doador buscarporId(Long id) throws Exception {
        return doadorRepository.buscarPorId(id)
                .orElseThrow(() -> new Exception("Doador não encontrado com a ID "+ id));
    }
}
