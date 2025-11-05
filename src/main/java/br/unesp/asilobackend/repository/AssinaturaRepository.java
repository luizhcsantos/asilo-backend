package br.unesp.asilobackend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import br.unesp.asilobackend.domain.Assinatura;

@Repository
public class AssinaturaRepository {

    private static final String FILE_NAME = "assinaturas.dat";
    private final ArquivoSerializer serializer;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public AssinaturaRepository() {
        this.serializer = new ArquivoSerializer();
        
        List<Assinatura> assinaturas = lerTodos();
        long maxId = assinaturas.stream()
            .map(Assinatura::getId)
            .filter(Objects::nonNull)
            .mapToLong(Long::longValue)
            .max()
            .orElse(0L);
        this.idGenerator.set(maxId);
    }

    private List<Assinatura> lerTodos() {
        Object data = serializer.lerArquivo(FILE_NAME);
        if (data instanceof List<?> list) {
            if (!list.isEmpty() && list.get(0) instanceof Assinatura) {
                return (List<Assinatura>) list;
            }
        }
        return new ArrayList<>();
    }

    public synchronized Assinatura salvar(Assinatura assinatura) {
        List<Assinatura> assinaturas = lerTodos();

        if (assinatura.getId() == null) {
            assinatura.setId(idGenerator.incrementAndGet());
            assinaturas.add(assinatura);
        } else {
            Optional<Assinatura> existente = assinaturas.stream()
                    .filter(a -> a.getId() != null && a.getId().equals(assinatura.getId()))
                    .findFirst();
            
            if (existente.isPresent()) {
                assinaturas.remove(existente.get());
            }
            assinaturas.add(assinatura);
        }

        boolean salvou = serializer.escreverArquivo(assinaturas, FILE_NAME);
        if (!salvou) {
            throw new RuntimeException("Erro ao salvar assinatura no arquivo.");
        }
        return assinatura;
    }

    public Optional<Assinatura> buscarPorId(Long id) {
        return lerTodos().stream()
                .filter(a -> a.getId() != null && a.getId().equals(id))
                .findFirst();
    }

    public List<Assinatura> buscarPorDoador(Long idDoador) {
        return lerTodos().stream()
                .filter(a -> a.getDoador() != null && a.getDoador().getId().equals(idDoador))
                .collect(Collectors.toList());
    }

    public List<Assinatura> buscarTodos() {
        return lerTodos();
    }
}
