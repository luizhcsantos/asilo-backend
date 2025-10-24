package br.unesp.asilobackend.service;

import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.repository.DoadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private DoadorRepository doadorRepository; // <-- Injetar repositório

    /**
     * Lista todos os doadores cadastrados.
     */
    public List<Doador> listarDoadores() {
        return doadorRepository.buscarTodos();
    }
}
