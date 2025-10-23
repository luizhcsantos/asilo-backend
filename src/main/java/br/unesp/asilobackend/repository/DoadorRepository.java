package br.unesp.asilobackend.repository;

import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.domain.PessoaFisica;
import br.unesp.asilobackend.domain.PessoaJuridica;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class DoadorRepository {

    private static final String FILE_NAME = "doadores.dat";
    private final ArquivoSerializer serializer;
    private final AtomicLong idGenerator = new AtomicLong(0);


    public DoadorRepository() {
        this.serializer = new ArquivoSerializer();

        List<Doador> doadores = lerTodos();
        long maxId = doadores.stream()
                .map(Doador::getDoadorId) // Primeiro mapeia para o objeto Long
                .filter(Objects::nonNull)   // Filtra os que são nulos
                .mapToLong(Long::longValue) // Converte para long (agora seguro)
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

    public boolean salvarTodos(List<Doador> doadores) {
		return serializer.escreverArquivo(doadores, FILE_NAME);
	}

    public synchronized Doador save(Doador doador) {
        List<Doador> doadores = lerTodos();

        // CORREÇÃO: DoadorId é Long (objeto), checar por 'null', não '== 0'
        if (doador.getDoadorId() == null) {
            doador.setDoadorId(idGenerator.incrementAndGet());
            doadores.add(doador);
        } else {
            Optional<Doador> existente = doadores.stream()
                    // CORREÇÃO: Checagem de nulo para evitar NPE na lista
                    .filter(d -> d.getDoadorId() != null && d.getDoadorId().equals(doador.getDoadorId()))
                    .findFirst();
            if (existente.isPresent()) {
                doadores.remove(existente.get());
            }
            // Adiciona o doador (novo ou atualizado)
            doadores.add(doador);
        }
        salvarTodos(doadores);
        return doador;
    }

    public Optional<Doador> buscarPorId(Long id) {
        return lerTodos().stream()
                // CORREÇÃO: Checagem de nulo para evitar NPE
                .filter(d -> d.getDoadorId() != null && d.getDoadorId().equals(id))
                .findFirst();
    }

    public List<Doador> findAll() {
        return lerTodos();
    }

    public Optional<Doador> buscarPorEmail(String email) {
        return lerTodos().stream()
                // CORREÇÃO: Checagem de nulo para evitar NPE
                .filter(d -> d.getDoadorEmail() != null && d.getDoadorEmail().equalsIgnoreCase(email))
                .findFirst();
    }


	public Optional<PessoaFisica> buscarPorCpf(String cpf) {
        return lerTodos().stream()
                .filter(d -> d instanceof PessoaFisica)
                .map(d -> (PessoaFisica) d)
                .filter(pf -> pf.getPessoaFisicaCpf() != null && pf.getPessoaFisicaCpf().equals(cpf))
                .findFirst();
	}

	public Optional<PessoaJuridica> buscarPorCnpj(String cnpj) {
        return lerTodos().stream()
                .filter(d -> d instanceof PessoaJuridica)
                .map(d -> (PessoaJuridica) d)
                .filter(pj -> pj.getPessoaJuridicaCnpj() != null && pj.getPessoaJuridicaCnpj().equals(cnpj))
                .findFirst();
	}

}
