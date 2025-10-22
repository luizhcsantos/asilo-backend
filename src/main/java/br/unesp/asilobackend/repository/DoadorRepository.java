package br.unesp.asilobackend.repository;

import br.unesp.asilobackend.repository.ArquivoSerializer;
import br.unesp.asilobackend.domain.Doador;

public class DoadorRepository {

	private ArquivoSerializer arquivoSerializer;

	public boolean salvar(Doador doador) {
		return false;
	}

	public Doador buscarPorCpf(String cpf) {
		return null;
	}

	public Doador buscarPorCnpj(String cnpj) {
		return null;
	}

}
