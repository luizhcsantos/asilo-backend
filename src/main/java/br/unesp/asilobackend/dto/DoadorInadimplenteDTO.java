package br.unesp.asilobackend.dto;

import br.unesp.asilobackend.domain.enums.TipoDoador;

public class DoadorInadimplenteDTO {

    private int doador_inadimplente_id_doador;

    private String doador_inadimplente_nome;

    private TipoDoador doador_inadimplente_tipo;

    private float doador_inadimplente_valor_em_atraso;
}
