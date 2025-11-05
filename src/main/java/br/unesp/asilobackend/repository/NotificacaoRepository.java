package br.unesp.asilobackend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import br.unesp.asilobackend.domain.Notificacao;

@Repository
public class NotificacaoRepository {

    private static final String FILE_NAME = "notificacoes.dat";
    private final ArquivoSerializer serializer;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public NotificacaoRepository() {
        this.serializer = new ArquivoSerializer();

        List<Notificacao> itens = lerTodos();
        long maxId = itens.stream()
                .map(Notificacao::getId)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .max()
                .orElse(0L);
        this.idGenerator.set(maxId);
    }

    @SuppressWarnings("unchecked")
    private List<Notificacao> lerTodos() {
        Object data = serializer.lerArquivo(FILE_NAME);
        if (data instanceof List<?> list) {
            if (!list.isEmpty() && list.get(0) instanceof Notificacao) {
                return (List<Notificacao>) list;
            }
        }
        return new ArrayList<>();
    }

    public synchronized Notificacao salvar(Notificacao notificacao) {
        List<Notificacao> all = lerTodos();

        if (notificacao.getId() == null) {
            notificacao.setId(idGenerator.incrementAndGet());
            all.add(notificacao);
        } else {
            Optional<Notificacao> existente = all.stream()
                    .filter(n -> n.getId() != null && n.getId().equals(notificacao.getId()))
                    .findFirst();
            existente.ifPresent(all::remove);
            all.add(notificacao);
        }

        boolean salvou = serializer.escreverArquivo(all, FILE_NAME);
        if (!salvou) {
            throw new RuntimeException("Erro ao salvar notificacao no arquivo.");
        }
        return notificacao;
    }

    public Optional<Notificacao> buscarPorId(Long id) {
        return lerTodos().stream()
                .filter(n -> n.getId() != null && n.getId().equals(id))
                .findFirst();
    }

    public List<Notificacao> buscarTodos() {
        return lerTodos();
    }

    public boolean salvarTodos(List<Notificacao> lista) {
        return serializer.escreverArquivo(lista, FILE_NAME);
    }
}
