package br.unesp.asilobackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndicadoresDTO {
    private double totalDoacoesMes;
    private int doadoresAtivos;
    private double mediaDoacaoPorDoador;
    private int totalInadimplentes;
    private int totalDoacoes;
}
