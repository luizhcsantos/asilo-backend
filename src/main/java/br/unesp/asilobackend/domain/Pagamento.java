package br.unesp.asilobackend.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import br.unesp.asilobackend.domain.enums.MeioPagamento;
import br.unesp.asilobackend.domain.enums.StatusPagamento;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Pagamento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private double valor;
    private Date data;
    private Date dataPagamento;
    private Date dataVencimento;
    private StatusPagamento status;
    private MeioPagamento meioPagamento;
    private Doacao doacao;
    private Assinatura assinatura;
    private String codigoPix;
    private String codigoBoleto;
}
