package br.unesp.asilobackend.domain;

import java.io.Serial;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Doador extends Usuario {

    @Serial
    private static final long serialVersionUID = 1L;

    private String telefone;
    private String nome;

    private List<Doacao> doacoes;
    private List<Assinatura> assinaturas;


}
