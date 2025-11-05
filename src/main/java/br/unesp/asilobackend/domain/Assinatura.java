package br.unesp.asilobackend.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.unesp.asilobackend.domain.enums.MeioPagamento;
import br.unesp.asilobackend.domain.enums.PeriodicidadeAssinatura;
import br.unesp.asilobackend.domain.enums.StatusAssinatura;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Assinatura implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private double valor;
    private StatusAssinatura status;
    private PeriodicidadeAssinatura periodicidade;
    private MeioPagamento meioPagamento;
    private Date dataInicio;
    private int diaPagamento;

    private Doador doador;
    private List<Pagamento> pagamentos;

}
