package br.unesp.asilobackend.dto;

import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.domain.PessoaFisica;
import br.unesp.asilobackend.domain.PessoaJuridica;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoadorListItemDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String tipo; // "PF" ou "PJ"

    public DoadorListItemDTO(Doador d) {
        this.id = d.getId();
        this.nome = d.getNome();
        this.email = d.getEmail();
        this.telefone = d.getTelefone();
        if (d instanceof PessoaFisica) this.tipo = "PF";
        else if (d instanceof PessoaJuridica) this.tipo = "PJ";
        else this.tipo = "";
    }
}
