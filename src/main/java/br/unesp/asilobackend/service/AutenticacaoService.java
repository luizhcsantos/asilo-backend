package br.unesp.asilobackend.service;

import br.unesp.asilobackend.domain.enums.TipoDoador;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

	public boolean login(TipoDoador tipo, String email, String senha) {
		return false;
	}

	public void recuperarSenha(String email) {

	}

    public String autenticar(String username, String password) {
        return null;
    }
}
