package br.unesp.asilobackend.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public abstract class Usuario implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String email;
    private String senhaHash;
}
