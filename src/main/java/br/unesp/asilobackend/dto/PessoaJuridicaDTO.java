package br.unesp.asilobackend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PessoaJuridicaDTO {

	private String pessoaJuridicaDtoRazaoSocial;

	private String pessoaJuridicaDtoNomeFantasia;

	private String pessoaJuridicaDtoCnpj;

    private String pessoaJuridicaDtoEmail;

    private String pessoaJuridicaDtoTelefone;

    private String pessoaJuridicaDtoSenha;

}
