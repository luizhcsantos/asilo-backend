package br.unesp.asilobackend.dto;

import java.util.Date;

import br.unesp.asilobackend.domain.enums.TipoRelatorio;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatorioDTO {
    private Long relatorioDtoId;
    private TipoRelatorio relatorioDtoTipo;
    private Date relatorioDtoDataInicio;
    private Date relatorioDtoDataFim;
    private double relatorioDtoValorArrecadado;
    private double relatorioDtoValorProjetado;
    private String relatorioDtoConteudo;
}
