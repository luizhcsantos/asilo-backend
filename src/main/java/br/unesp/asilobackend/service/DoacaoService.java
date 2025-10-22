package br.unesp.asilobackend.service;

import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.dto.DoacaoCreateDTO;
import br.unesp.asilobackend.dto.DoacaoDetailDTO;

import java.util.List;

public class DoacaoService {

	public boolean criarDoacao(Doador doador, DoacaoCreateDTO doacao) {
		return false;
	}

	public DoacaoDetailDTO obterDetalhes(int idDoacao) {
		return null;
	}

	public List<DoacaoDetailDTO> obterDoacaoDoador(int idDoador) {
		return null;
	}

	public List<DoacaoDetailDTO> obterTodasDoacoes() {
		return null;
	}

}
