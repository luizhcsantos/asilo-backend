package br.unesp.asilobackend.domain;

import br.unesp.asilobackend.domain.enums.TipoNotificacao;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
public class Notificacao implements Serializable  {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private TipoNotificacao tipo;
    private String mensagem;
    private Date dataEnvio;
    private Doador destinatario;
    private Administrador administrador;

}
