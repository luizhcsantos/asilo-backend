package br.unesp.asilobackend.domain;

import br.unesp.asilobackend.domain.enums.TipoDoacao;
import br.unesp.asilobackend.domain.enums.MeioPagamento;

import java.io.Serializable;
import java.util.Date;

public class Doacao implements Serializable {

    private static final long serialVersionUID = 1L;

    private int doacao_id;

	private float doacao_valor;

	private TipoDoacao doacao_tipo;

	private Date doacao_data;

	private boolean doacao_status;

	private MeioPagamento doacao_meio_pagamento;

	private boolean doacao_anonima;

	private int assinatura_id;

	private int doador_id;

}
