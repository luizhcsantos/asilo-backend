package br.unesp.asilobackend.dto;

import br.unesp.asilobackend.domain.Administrador;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminDTO {
    private Long adminDtoId;
    private String adminDtoNome;
    private String adminDtoEmail;
    private String adminDtoSenha;  // Usado apenas para receber a senha no cadastro

    /**
     * Construtor para facilitar a conversão da Entidade para DTO
     * Propositalmente NÃO copia a senha (senhaHash)
     */
    public AdminDTO(Administrador admin) {
        this.adminDtoId = admin.getId();
        this.adminDtoNome = admin.getNome();
        this.adminDtoEmail = admin.getEmail();
    }
}
