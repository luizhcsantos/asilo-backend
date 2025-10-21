package br.unesp.asilobackend.domain;

import java.util.Date;

public class Pagamento {

    private int id;
    private float valor;
    private Date data_emissao;
    private Date data_vencimento;
    private Date data_pagamento;
    private boolean status;
    private int doacao_id;
}
