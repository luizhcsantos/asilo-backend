package br.unesp.asilobackend.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import br.unesp.asilobackend.domain.Doacao;
import br.unesp.asilobackend.dto.PagamentoDTO;

@Repository
public class DoacaoRepository {

    private static final String FILE_NAME = "doacoes.dat";
    private final ArquivoSerializer serializer;
    private final AtomicLong idGenerator = new AtomicLong(0);

    public DoacaoRepository() {
        this.serializer = new ArquivoSerializer();
        
        List<Doacao> doacoes = lerTodos();
        long maxId = doacoes.stream()
            .map(Doacao::getId)
            .filter(Objects::nonNull)
            .mapToLong(Long::longValue)
            .max()
            .orElse(0L);
        this.idGenerator.set(maxId);
    }

    private List<Doacao> lerTodos() {
        Object data = serializer.lerArquivo(FILE_NAME);
        if (data instanceof List<?> list) {
            if (!list.isEmpty() && list.get(0) instanceof Doacao) {
                return (List<Doacao>) list;
            }
        }
        return new ArrayList<>();
    }

    public synchronized Doacao salvar(Doacao doacao) {
        List<Doacao> doacoes = lerTodos();

        if (doacao.getId() == null) {
            doacao.setId(idGenerator.incrementAndGet());
            doacoes.add(doacao);
        } else {
            Optional<Doacao> existente = doacoes.stream()
                    .filter(d -> d.getId() != null && d.getId().equals(doacao.getId()))
                    .findFirst();
            
            if (existente.isPresent()) {
                doacoes.remove(existente.get());
            }
            doacoes.add(doacao);
        }

        boolean salvou = serializer.escreverArquivo(doacoes, FILE_NAME);
        if (!salvou) {
            throw new RuntimeException("Erro ao salvar doação no arquivo.");
        }
        return doacao;
    }

    public Optional<Doacao> buscarPorId(Long idDoacao) {
        return lerTodos().stream()
                .filter(d -> d.getId() != null && d.getId().equals(idDoacao))
                .findFirst();
    }

    public List<Doacao> buscarPorDoador(Long idDoador) {
        return lerTodos().stream()
                .filter(d -> d.getDoador() != null && d.getDoador().getId().equals(idDoador))
                .collect(Collectors.toList());
    }

    public List<Doacao> buscarTodos() {
        return lerTodos();
    }

    public List<PagamentoDTO> obterDaocaoDoador(long doadorId) {
        return lerTodos().stream()
        .filter(doacao -> doacao.getDoador() != null &&
        doacao.getDoador().getId().equals(doadorId))
        .map(Doacao::getPagamento)
        .filter(Objects::nonNull)
        .map(p -> {
            PagamentoDTO dto = new PagamentoDTO(p.getId(), p.getValor());
            dto.setPagamentoDtoData(p.getData());
            dto.setPagamentoDtoDataPagamento(p.getDataPagamento());
            dto.setPagamentoDtoDataVencimento(p.getDataVencimento());
            dto.setPagamentoDtoStatus(p.getStatus());
            dto.setPagamentoDtoMeioPagamento(p.getMeioPagamento());
            dto.setPagamentoDtoCodigoPix(p.getCodigoPix());
            dto.setPagamentoDtoCodigoBoleto(p.getCodigoBoleto());
            return dto;
        })
        .collect(Collectors.toList());
    }
}
