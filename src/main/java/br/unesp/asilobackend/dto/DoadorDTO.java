package br.unesp.asilobackend.dto;

import br.unesp.asilobackend.domain.Doador;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DoadorDTO {

    /**
     * Construtor que mapeia o domínio Doador para DoadorDTO
     * @param doador
     */
    public DoadorDTO(Doador doador) {
        this.id = doador.getId();
        this.nome = doador.getNome();
        this.email = doador.getEmail();
        this.telefone = doador.getTelefone();
    }

    private Long id;
    private String nome;
    private String email;
    private String telefone;

}
