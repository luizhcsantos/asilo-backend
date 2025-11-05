package br.unesp.asilobackend.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Setter
@Getter
public class Doacao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Date data;
    private Doador doador;
    // Associação 1-para-1 com Pagamento
    private Pagamento pagamento;
    private List<DoacaoItem> itens;


}
