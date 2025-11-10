package br.unesp.asilobackend.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoacaoListItemDTO {
    private Long id;
    private double valor;
    private Date data;
    private String descricao;
}
