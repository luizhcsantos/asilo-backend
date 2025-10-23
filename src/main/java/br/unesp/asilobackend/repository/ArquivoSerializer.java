package br.unesp.asilobackend.repository;

import br.unesp.asilobackend.domain.Administrador;
import br.unesp.asilobackend.domain.Doador;

import java.util.List;

public class ArquivoSerializer {

	public boolean escreverArquivo(List<Doador> doadores, String caminho) {
		return false;
	}

	public int lerArquivo(String caminho) {
		return 0;
	}

    public boolean escreverArquivoAdmin(List<Administrador> admins, String fileName) {
        return false;
    }
}
