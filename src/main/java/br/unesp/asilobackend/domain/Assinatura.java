package br.unesp.asilobackend.domain;

import br.unesp.asilobackend.domain.enums.MeioPagamento;
import br.unesp.asilobackend.domain.enums.PeriodicidadeAssinatura;

import java.io.Serializable;
import java.util.Date;

public class Assinatura implements Serializable {

    private static final long serialVersionUID = 1L;

    private int assinatura_id;

	private float assinatura_valor;

	private PeriodicidadeAssinatura assinatura_periodicidade;

	private MeioPagamento assinatura_meio_pagamento;

	private Date assinatura_data_inicio;

	private boolean assinatura_status;

	private int doador_id;

}
