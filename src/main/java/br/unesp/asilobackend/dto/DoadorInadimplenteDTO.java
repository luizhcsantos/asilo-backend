package br.unesp.asilobackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoadorInadimplenteDTO {
    private Long doadorInadiplenteDtoId;
    private String doadorInadiplenteDtoNome;
    private String doadorInadiplenteDtoEmail;
    private String doadorInadiplenteDtoTelefone;
    private int doadorInadiplenteDtoDiasAtraso;
    private double doadorInadiplenteDtoValorEmAtraso;
    private String doadorInadiplenteDtoTipo;  // PF ou PJ
}
