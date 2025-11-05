package br.unesp.asilobackend.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class DoacaoItem implements Serializable {

    private Long id;
    private int quantidade;
    private String descricao;
    private Doacao doacao;
}
