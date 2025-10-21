package br.unesp.asilobackend.domain;

import br.unesp.asilobackend.domain.enums.MeioPagamento;
import br.unesp.asilobackend.domain.enums.TipoDoacao;

import java.util.Date;

public class Doacao {

    private int doacao_id;
    private float valor;
    private Date data_doacao;
    private TipoDoacao tipo_doacao;
    private boolean doacao_status;
    private MeioPagamento meio_pagamento;
    private boolean anonima;
    private int doador_id;
}
