package br.unesp.asilobackend.domain;

import br.unesp.asilobackend.domain.enums.TipoNotificacao;

import java.io.Serializable;
import java.util.Date;

public class Notificacao implements Serializable  {

    private static final long serialVersionUID = 1L;

    private int notificacao_id;

	private TipoNotificacao notificacao_tipo;

	private Date notificacao_data_envio;

	private String notificacao_destino;

}
