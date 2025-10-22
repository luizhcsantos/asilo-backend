package br.unesp.asilobackend.service;

import br.unesp.asilobackend.domain.Pagamento;
import br.unesp.asilobackend.domain.Doacao;
import br.unesp.asilobackend.dto.PagamentoDTO;

public class PagamentoService {

	public Pagamento gerarPagamento(Doacao doacao) {
		return null;
	}

	public boolean confirmarPagamento(int idPagamento) {
		return false;
	}

	public Pagamento reemitirBoleto(int idPagamento) {
		return null;
	}

	public String gerarPix(int idPagmento) {
		return null;
	}

	public PagamentoDTO buscarPagamentoPorDoacao(int idDoacao) {
		return null;
	}

}
