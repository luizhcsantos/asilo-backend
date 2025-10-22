package br.unesp.asilobackend.repository;

import br.unesp.asilobackend.repository.ArquivoSerializer;
import br.unesp.asilobackend.domain.Pagamento;

import java.util.List;

public class PagamentoRepository  {

	private ArquivoSerializer arquivoSerializer;

	public boolean salvarPagamento(Pagamento pagamento) {
		return false;
	}

	public Pagamento buscarPorId(int id) {
		return null;
	}

	public List<Pagamento> buscarAll() {
		return null;
	}

}
