package br.unesp.asilobackend.dto;

import java.util.Date;

import br.unesp.asilobackend.domain.Assinatura;
import br.unesp.asilobackend.domain.enums.MeioPagamento;
import br.unesp.asilobackend.domain.enums.PeriodicidadeAssinatura;
import br.unesp.asilobackend.domain.enums.StatusAssinatura;
import br.unesp.asilobackend.domain.enums.StatusPagamento;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AssinaturaDTO {

    // Campos necessários para criação e exibição
    private Long id;
    private double valor;
    private StatusAssinatura status;
    private PeriodicidadeAssinatura periodicidade;
    private MeioPagamento meioPagamento;
    private StatusPagamento statusPagamento;
    private Date dataPagamento;
    private Date dataInicio;

    // Construtor para listagens simples
    public AssinaturaDTO(Long id, double valor, StatusAssinatura status) {
        this.id = id;
        this.valor = valor;
        this.status = status;
    }

    // Construtor de conversão a partir da entidade
    public AssinaturaDTO(Assinatura assinatura) {
        if (assinatura != null) {
            this.id = assinatura.getId();
            this.valor = assinatura.getValor();
            this.status = assinatura.getStatusAssinatura();
            this.periodicidade = assinatura.getPeriodicidade();
            this.meioPagamento = assinatura.getMeioPagamento();
            this.statusPagamento = assinatura.getStatusPagamento();
            this.dataInicio = assinatura.getDataInicio();
        }
    }
}
