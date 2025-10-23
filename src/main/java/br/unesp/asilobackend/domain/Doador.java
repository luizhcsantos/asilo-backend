package br.unesp.asilobackend.domain;

import br.unesp.asilobackend.domain.enums.TipoDoador;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class Doador implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long doadorId;

    private String doadorTelefone;

    private String doadorEmail;

    private String doadorSenha;

    private Date doadorDataCriacao;

    private TipoDoador doadorTipo;


}
