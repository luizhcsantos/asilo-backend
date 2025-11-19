package br.unesp.asilobackend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import br.unesp.asilobackend.domain.Administrador;

@Repository
public class AdministradorRepository {

    private static final String FILE_NAME = "admins.dat";
    private final ArquivoSerializer serializer;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public AdministradorRepository() {
        this.serializer = new ArquivoSerializer();

    List<Administrador> admins = lerTodos();
    long maxId = admins.stream()
        .map(Administrador::getId)
        .filter(Objects::nonNull)
        .mapToLong(Long::longValue)
        .max()
        .orElse(0L);
        this.idGenerator.set(maxId);
    }

    public List<Administrador> lerTodos() {
        Object data = serializer.lerArquivo(FILE_NAME);
        if (data instanceof List<?> list) {
            // CORREÇÃO: Deve checar por Administrador, não Doador
            if (!list.isEmpty() && list.get(0) instanceof Administrador) {
                return (List<Administrador>) list;
            }
        }
        return new ArrayList<>();
    }

    public Optional<Administrador> buscarPorEmail(String email) {
        return lerTodos().stream()
                .filter(a -> a.getEmail() != null && a.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    public synchronized Administrador salvar(Administrador admin) {
        List<Administrador> todos = lerTodos();
        if (admin.getId() == null) {
            long nextId = idGenerator.incrementAndGet();
            admin.setId(nextId);
        } else {
            // remove qualquer existente com o mesmo id para atualizar
            todos.removeIf(a -> Objects.equals(a.getId(), admin.getId()));
        }
        todos.add(admin);
        serializer.escreverArquivo(todos, FILE_NAME);
        return admin;
    }

    public synchronized void salvarTodos(List<Administrador> admins) {
        long maxId = admins.stream()
            .map(Administrador::getId)
            .filter(Objects::nonNull)
            .mapToLong(Long::longValue)
            .max()
            .orElse(0L);
        this.idGenerator.set(maxId);
        serializer.escreverArquivo(admins, FILE_NAME);
    }
}