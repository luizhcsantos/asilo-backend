package br.unesp.asilobackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaJuridicaRequestDTO {

    private String pessoaJuridicaDtoNomeFantasia;

    private String pessoaJuridicaDtoCnpj;

    private String pessoaJuridicaDtoEmail;

    private String pessoaJuridicaDtoTelefone;

    private String pessoaJuridicaDtoSenha;

}
