package br.unesp.asilobackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaFisicaRequestDTO {

    private String pessoaFisicaDtoNome;

    private String pessoaFisicaDtoCpf;

    private String pessoaFisicaDtoEmail;

    private String pessoaFisicaDtoTelefone;

    private String pessoaFisicaDtoSenha;

}
