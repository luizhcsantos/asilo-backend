package br.unesp.asilobackend.dto;

import java.util.Date;

import br.unesp.asilobackend.domain.enums.MeioPagamento;
import br.unesp.asilobackend.domain.enums.StatusPagamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoDTO {

    public PagamentoDTO(Long id, double valor) {
        this.pagamentoDtoId = id;
        this.pagamentoDtoValor = valor;
    }
    private Long pagamentoDtoId;
    private double pagamentoDtoValor;
    private Date pagamentoDtoData;
    private Date pagamentoDtoDataPagamento;
    private Date pagamentoDtoDataVencimento;
    private StatusPagamento pagamentoDtoStatus;
    private MeioPagamento pagamentoDtoMeioPagamento;
    private String pagamentoDtoCodigoPix;
    private String pagamentoDtoCodigoBoleto;

}
