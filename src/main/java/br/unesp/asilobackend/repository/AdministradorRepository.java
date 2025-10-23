package br.unesp.asilobackend.repository;

import br.unesp.asilobackend.domain.Administrador;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AdministradorRepository {

    private static final String FILE_NAME = "admins.dat";
    private final ArquivoSerializer serializer;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public AdministradorRepository() {
        this.serializer = new ArquivoSerializer();

        List<Administrador> admins = lerTodos();
        long maxId = admins.stream()
                .mapToLong(Administrador::getAdminId)
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
                .filter(a -> a.getAdminEmail() != null && a.getAdminEmail().equalsIgnoreCase(email))
                .findFirst();
    }
}