package br.unesp.asilobackend.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class Administrador implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

	private int adminId;

	private String adminNome;

	private String adminEmail;

	private String adminSenha;

}
