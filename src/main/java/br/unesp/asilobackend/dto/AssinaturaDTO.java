package br.unesp.asilobackend.dto;

import br.unesp.asilobackend.domain.enums.PeriodicidadeAssinatura;
import br.unesp.asilobackend.domain.enums.MeioPagamento;

public class AssinaturaDTO {

	private PeriodicidadeAssinatura assinatura_dto_periocidade;

	private float assinatura_dto_valor;

	private MeioPagamento assinatura_dto_meio_pagamento;

	private boolean assinatura_dto_status;

}
