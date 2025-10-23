package br.unesp.asilobackend.domain;

import java.io.Serializable;

public class Relatorio implements Serializable {

    private static final long serialVersionUID = 1L;

    private int relatorio_id;

	private String relatorio_periodo;

	private float relatorio_valor_arrecadado;

	private float relatorio_valor_projetado;

}
