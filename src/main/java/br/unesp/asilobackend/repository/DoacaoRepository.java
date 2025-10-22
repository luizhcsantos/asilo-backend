package br.unesp.asilobackend.repository;

import br.unesp.asilobackend.repository.ArquivoSerializer;
import br.unesp.asilobackend.domain.Doacao;

import java.util.List;

public class DoacaoRepository {

	private ArquivoSerializer arquivoSerializer;

	public boolean salvar(Doacao doacao) {
		return false;
	}

	public Doacao buscarPorId(int idDoacao) {
		return null;
	}

	public Doacao buscarPorDoador(int idDoaador) {
		return null;
	}

	public List<Doacao> buscarAll() {
		return null;
	}

}
