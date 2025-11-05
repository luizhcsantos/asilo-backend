package br.unesp.asilobackend.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class Administrador extends Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

	private String nome;

}
