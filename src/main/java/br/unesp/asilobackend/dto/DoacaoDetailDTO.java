package br.unesp.asilobackend.dto;

import br.unesp.asilobackend.domain.enums.TipoDoacao;
import br.unesp.asilobackend.domain.enums.MeioPagamento;

import java.util.Date;

public class DoacaoDetailDTO {

	private int doacao_detail_dto_id_doacao;

	private float doacao_detail_dto_valor;

	private TipoDoacao doacao_detail_dto_tipo;

	private Date doacao_detail_dto_data;

	private boolean doacao_detail_dto_status;

	private MeioPagamento doacao_detail_dto_meio_pagamento;

	private boolean doacao_detail_dto_anonima;

}
