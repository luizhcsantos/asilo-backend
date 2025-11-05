package br.unesp.asilobackend.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Doador extends Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String telefone;
    private String nome;

    private List<Doacao> doacoes;
    private List<Assinatura> assinaturas;


}
