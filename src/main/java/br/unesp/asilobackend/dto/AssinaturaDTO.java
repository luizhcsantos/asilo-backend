package br.unesp.asilobackend.dto;

import java.util.Date;

import br.unesp.asilobackend.domain.enums.MeioPagamento;
import br.unesp.asilobackend.domain.enums.PeriodicidadeAssinatura;
import br.unesp.asilobackend.domain.enums.StatusAssinatura;
import br.unesp.asilobackend.domain.enums.StatusPagamento;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AssinaturaDTO {

	public AssinaturaDTO(Long id, double valor, StatusAssinatura status) {

	}

	private PeriodicidadeAssinatura periodicidade;
	private float valor;
	private MeioPagamento meioPagamento;
	private StatusPagamento statusPagamento;
	private Date dataPagamento;
	private Date dataInicio;  

}
