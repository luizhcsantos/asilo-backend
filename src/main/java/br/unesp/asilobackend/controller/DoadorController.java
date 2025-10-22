package br.unesp.asilobackend.controller;

import br.unesp.asilobackend.dto.PessoaFisicaDTO;
import br.unesp.asilobackend.dto.PessoaJuridicaDTO;
import br.unesp.asilobackend.dto.DoacaoCreateDTO;
import br.unesp.asilobackend.dto.DoacaoDetailDTO;
import br.unesp.asilobackend.dto.AssinaturaDTO;

import java.util.List;

public class DoadorController {

	public boolean realizarCadastroPessoaFisica(PessoaFisicaDTO pessoa) {
		return false;
	}

	public boolean realizarCadastroPessoaJuridica(PessoaJuridicaDTO pessoa) {
		return false;
	}

	public boolean efetuarLogin(String emal, String senha) {
		return false;
	}

	public boolean realizarDoacao(DoacaoCreateDTO doacao) {
		return false;
	}

	public DoacaoDetailDTO consultarDoacao(int id) {
		return null;
	}

	public void gerenciarAssianatura(AssinaturaDTO assinatura) {

	}

	public List<DoacaoDetailDTO> consultarHistoricoDoacao(int idDoador) {
		return null;
	}

	public boolean recuperarSenha(String email) {
		return false;
	}

}
