package br.unesp.asilobackend.domain;

import br.unesp.asilobackend.domain.enums.MeioPagamento;
import br.unesp.asilobackend.domain.enums.PeriodicidadeAssinatura;

import java.util.Date;

public class Assinatura {

    private int id;
    private float valor;
    private PeriodicidadeAssinatura periodicidade;
    private MeioPagamento meio_pagamento;
    private Date data_inicio;
    private boolean status;
    private int doador_id;

}
