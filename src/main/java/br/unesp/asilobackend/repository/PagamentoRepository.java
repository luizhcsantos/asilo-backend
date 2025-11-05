package br.unesp.asilobackend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import br.unesp.asilobackend.domain.Pagamento;

@Repository
public class PagamentoRepository {

    private static final String FILE_NAME = "pagamentos.dat";
    private final ArquivoSerializer serializer;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public PagamentoRepository() {
        this.serializer = new ArquivoSerializer();
        
        List<Pagamento> pagamentos = lerTodos();
        long maxId = pagamentos.stream()
            .map(Pagamento::getId)
            .filter(Objects::nonNull)
            .mapToLong(Long::longValue)
            .max()
            .orElse(0L);
        this.idGenerator.set(maxId);
    }

    private List<Pagamento> lerTodos() {
        Object data = serializer.lerArquivo(FILE_NAME);
        if (data instanceof List<?> list) {
            if (!list.isEmpty() && list.get(0) instanceof Pagamento) {
                return (List<Pagamento>) list;
            }
        }
        return new ArrayList<>();
    }

    public synchronized Pagamento salvar(Pagamento pagamento) {
        List<Pagamento> pagamentos = lerTodos();

        if (pagamento.getId() == null) {
            pagamento.setId(idGenerator.incrementAndGet());
            pagamentos.add(pagamento);
        } else {
            Optional<Pagamento> existente = pagamentos.stream()
                    .filter(p -> p.getId() != null && p.getId().equals(pagamento.getId()))
                    .findFirst();
            
            if (existente.isPresent()) {
                pagamentos.remove(existente.get());
            }
            pagamentos.add(pagamento);
        }

        boolean salvou = serializer.escreverArquivo(pagamentos, FILE_NAME);
        if (!salvou) {
            throw new RuntimeException("Erro ao salvar pagamento no arquivo.");
        }
        return pagamento;
    }

    public Optional<Pagamento> buscarPorId(Long id) {
        return lerTodos().stream()
                .filter(p -> p.getId() != null && p.getId().equals(id))
                .findFirst();
    }

    public List<Pagamento> buscarTodos() {
        return lerTodos();
    }
}
