package br.unesp.asilobackend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import br.unesp.asilobackend.domain.Relatorio;

@Repository
public class RelatorioRepository {

    private static final String FILE_NAME = "relatorios.dat";
    private final ArquivoSerializer serializer;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public RelatorioRepository() {
        this.serializer = new ArquivoSerializer();
        
        List<Relatorio> relatorios = lerTodos();
        long maxId = relatorios.stream()
            .map(Relatorio::getId)
            .filter(Objects::nonNull)
            .mapToLong(Long::longValue)
            .max()
            .orElse(0L);
        this.idGenerator.set(maxId);
    }

    private List<Relatorio> lerTodos() {
        Object data = serializer.lerArquivo(FILE_NAME);
        if (data instanceof List<?> list) {
            if (!list.isEmpty() && list.get(0) instanceof Relatorio) {
                return (List<Relatorio>) list;
            }
        }
        return new ArrayList<>();
    }

    public synchronized Relatorio salvar(Relatorio relatorio) {
        List<Relatorio> relatorios = lerTodos();

        if (relatorio.getId() == null) {
            relatorio.setId(idGenerator.incrementAndGet());
            relatorios.add(relatorio);
        } else {
            Optional<Relatorio> existente = relatorios.stream()
                    .filter(r -> r.getId() != null && r.getId().equals(relatorio.getId()))
                    .findFirst();
            
            if (existente.isPresent()) {
                relatorios.remove(existente.get());
            }
            relatorios.add(relatorio);
        }

        boolean salvou = serializer.escreverArquivo(relatorios, FILE_NAME);
        if (!salvou) {
            throw new RuntimeException("Erro ao salvar relatório no arquivo.");
        }
        return relatorio;
    }

    public Optional<Relatorio> buscarPorId(Long id) {
        return lerTodos().stream()
                .filter(r -> r.getId() != null && r.getId().equals(id))
                .findFirst();
    }

    public List<Relatorio> buscarTodos() {
        return lerTodos();
    }
}
