package br.unesp.asilobackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssinaturaListItemDTO {
    private Long id;
    private boolean ativa;
    private String plano;
}
