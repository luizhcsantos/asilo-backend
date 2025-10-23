package br.unesp.asilobackend.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class PessoaJuridica extends Doador implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String pessoaJuridicaNome;

	private String pessoaJuridicaCnpj;

	private String pessoaJuridicaNomeFantasia;

}
