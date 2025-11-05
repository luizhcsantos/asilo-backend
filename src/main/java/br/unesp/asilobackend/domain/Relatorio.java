package br.unesp.asilobackend.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.unesp.asilobackend.domain.enums.TipoRelatorio;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Relatorio implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private TipoRelatorio tipo;
    private Date dataGeracao;
    private Date dataInicio;
    private Date dataFim;
    private String conteudo;
    private List<Doacao> doacoes;
    private List<Assinatura> assinaturas;
    private List<Doador> doadores;

    private Administrador administrador;
}

