package br.unesp.asilobackend.dto;

import br.unesp.asilobackend.domain.enums.MeioPagamento;
import br.unesp.asilobackend.domain.enums.TipoDoacao;

public class DoacaoCreateDTO {

	private MeioPagamento doacao_create_dto_meio_pagamento;

	private TipoDoacao doacao_create_dto_tipo;

	private boolean doacao_create_dto_anonima;

	private float doacao_create_dto_valor;

}
