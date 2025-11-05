package br.unesp.asilobackend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.domain.PessoaFisica;
import br.unesp.asilobackend.domain.PessoaJuridica;

@Repository
public class DoadorRepository {

    private static final String FILE_NAME = "doadores.dat";
    private final ArquivoSerializer serializer;
    private final AtomicLong idGenerator = new AtomicLong(0);


    public DoadorRepository() {
        this.serializer = new ArquivoSerializer();

    List<Doador> doadores = lerTodos();
    long maxId = doadores.stream()
        .map(Doador::getId) // usa getId do Usuario
        .filter(Objects::nonNull)
        .mapToLong(Long::longValue)
        .max()
        .orElse(0L);
        this.idGenerator.set(maxId);
    }

    private List<Doador> lerTodos() {
        Object data = serializer.lerArquivo(FILE_NAME);
        if (data instanceof List<?> list) {
            // Verifica se os objetos na lista são instâncias de Doador
            if (!list.isEmpty() && list.get(0) instanceof Doador) {
                return (List<Doador>) list;
            }
        }
        return new ArrayList<>();
    }

    public void salvarTodos(List<Doador> doadores) {
		serializer.escreverArquivo(doadores, FILE_NAME);
	}

    public synchronized Doador save(Doador doador) {
        List<Doador> doadores = lerTodos();

        if (doador.getId() == null) {
            doador.setId(idGenerator.incrementAndGet());
            doadores.add(doador);
        } else {
            Optional<Doador> existente = doadores.stream()
                    // Checagem de nulo para evitar NPE na lista
                    .filter(d -> d.getId() != null && d.getId().equals(doador.getId()))
                    .findFirst();
            if (existente.isPresent()) {
                doadores.remove(existente.get());
            }
            // Adiciona o doador (novo ou atualizado)
            doadores.add(doador);
        }
        boolean salvou = serializer.escreverArquivo(doadores, FILE_NAME);
        if (!salvou) {
            throw new RuntimeException("Erro ao salvar no arquivo.");
        }
        return doador;
    }

    public Optional<Doador> buscarPorId(Long id) {
        return lerTodos().stream()
                // Checagem de nulo para evitar NPE
                .filter(d -> d.getId() != null && d.getId().equals(id))
                .findFirst();
    }

    public List<Doador> buscarTodos() {
        return lerTodos();
    }

    public Optional<Doador> buscarPorEmail(String email) {
        return lerTodos().stream()
                // CORREÇÃO: Checagem de nulo para evitar NPE
                .filter(d -> d.getEmail() != null && d.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }


	public Optional<PessoaFisica> buscarPorCpf(String cpf) {
        return lerTodos().stream()
                .filter(d -> d instanceof PessoaFisica)
                .map(d -> (PessoaFisica) d)
                .filter(pf -> pf.getCpf() != null && pf.getCpf().equals(cpf))
                .findFirst();
	}

	public Optional<PessoaJuridica> buscarPorCnpj(String cnpj) {
        return lerTodos().stream()
                .filter(d -> d instanceof PessoaJuridica)
                .map(d -> (PessoaJuridica) d)
        .filter(pj -> pj.getCnpj() != null && pj.getCnpj().equals(cnpj))
                .findFirst();
	}

}
