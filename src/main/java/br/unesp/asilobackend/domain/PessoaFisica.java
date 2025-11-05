package br.unesp.asilobackend.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class PessoaFisica extends Doador implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

	private String cpf;

}
