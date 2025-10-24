package br.unesp.asilobackend.controller;

import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.domain.PessoaFisica;
import br.unesp.asilobackend.domain.PessoaJuridica;
import br.unesp.asilobackend.dto.PessoaFisicaDTO;
import br.unesp.asilobackend.dto.PessoaJuridicaDTO;
import br.unesp.asilobackend.dto.DoacaoCreateDTO;
import br.unesp.asilobackend.dto.DoacaoDetailDTO;
import br.unesp.asilobackend.dto.AssinaturaDTO;
import br.unesp.asilobackend.service.DoadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doador")
public class DoadorController {

    @Autowired
    private DoadorService doadorService;

    @PostMapping("/pf")
	public ResponseEntity<?> realizarCadastroPessoaFisica(@RequestBody PessoaFisicaDTO pessoa) {
        try {
            PessoaFisica novoDoador = doadorService.salvarPessoaFisica(pessoa);
            return ResponseEntity.ok(novoDoador);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
	}

    @PostMapping("/pj")
	public ResponseEntity<?> realizarCadastroPessoaJuridica(PessoaJuridicaDTO pessoa) {
        try {
            PessoaJuridica novoDoador = doadorService.salvarPessoaJuridica(pessoa);
            return ResponseEntity.ok(novoDoador);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
	}

	public boolean efetuarLogin(String email, String senha) {
		return false;
	}

	public boolean realizarDoacao(DoacaoCreateDTO doacao) {
		return false;
	}

	public DoacaoDetailDTO consultarDoacao(int id) {
		return null;
	}

	public void gerenciarAssinatura(AssinaturaDTO assinatura) {

	}

	public List<DoacaoDetailDTO> consultarHistoricoDoacao(int idDoador) {
		return null;
	}

	public boolean recuperarSenha(String email) {
		return false;
	}

    public ResponseEntity<?> getDoadorPorId(@PathVariable Long id) {
        try {
            Doador doador = doadorService.buscarporId(id);
            return ResponseEntity.ok(doador);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

}
