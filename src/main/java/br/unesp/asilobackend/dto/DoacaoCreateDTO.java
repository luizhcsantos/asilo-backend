package br.unesp.asilobackend.dto;

import br.unesp.asilobackend.domain.enums.MeioPagamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoacaoCreateDTO {

	private MeioPagamento meioPagamento;
	private TipoDoacao tipo;
	private boolean anonima;
	private double valor;

}
