package br.unesp.asilobackend.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unesp.asilobackend.domain.Assinatura;
import br.unesp.asilobackend.domain.Doacao;
import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.domain.Relatorio;
import br.unesp.asilobackend.domain.enums.StatusAssinatura;
import br.unesp.asilobackend.domain.enums.StatusPagamento;
import br.unesp.asilobackend.domain.enums.TipoRelatorio;
import br.unesp.asilobackend.dto.DoadorInadimplenteDTO;
import br.unesp.asilobackend.dto.IndicadoresDTO;
import br.unesp.asilobackend.dto.RelatorioDTO;
import br.unesp.asilobackend.repository.AssinaturaRepository;
import br.unesp.asilobackend.repository.DoacaoRepository;
import br.unesp.asilobackend.repository.DoadorRepository;
import br.unesp.asilobackend.repository.RelatorioRepository;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Autowired
    private AssinaturaRepository assinaturaRepository;

    @Autowired
    private DoadorRepository doadorRepository;

    public IndicadoresDTO consultarIndicadores() {
        IndicadoresDTO indicadores = new IndicadoresDTO();
        
        // Calcula total de doações no mês
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date inicioMes = cal.getTime();
        
        double totalDoacoesMes = doacaoRepository.buscarTodos().stream()
            .filter(d -> d.getData().after(inicioMes) && 
                        d.getPagamento() != null && 
                        d.getPagamento().getStatus() == StatusPagamento.PAGO)
            .mapToDouble(d -> d.getPagamento().getValor())
            .sum();
        
        // Total de doadores ativos (com doação ou assinatura nos últimos 3 meses)
        cal.add(Calendar.MONTH, -3);
        Date tresMesesAtras = cal.getTime();
        
        long doadoresAtivos = doadorRepository.buscarTodos().stream()
            .filter(d -> {
                boolean temDoacao = doacaoRepository.buscarPorDoador(d.getId()).stream()
                    .anyMatch(doacao -> doacao.getData().after(tresMesesAtras));
                boolean temAssinatura = assinaturaRepository.buscarPorDoador(d.getId()).stream()
                    .anyMatch(a -> a.getStatus() == StatusAssinatura.ATIVA);
                return temDoacao || temAssinatura;
            })
            .count();

        // Calcula média de doação por doador
        double mediaDoacaoPorDoador = totalDoacoesMes / (doadoresAtivos > 0 ? doadoresAtivos : 1);

        indicadores.setTotalDoacoesMes(totalDoacoesMes);
        indicadores.setDoadoresAtivos((int) doadoresAtivos);
        indicadores.setMediaDoacaoPorDoador(mediaDoacaoPorDoador);

        return indicadores;
    }

    public List<DoadorInadimplenteDTO> consultarInadiplentes() {
        List<DoadorInadimplenteDTO> inadimplentes = new ArrayList<>();
        
        // Busca todas as assinaturas inativas
        List<Assinatura> assinaturasInativas = assinaturaRepository.buscarTodos().stream()
            .filter(a -> a.getStatus() == StatusAssinatura.INATIVA)
            .collect(Collectors.toList());
        
        // Para cada assinatura, cria um DTO com informações do doador
        for (Assinatura assinatura : assinaturasInativas) {
            Doador doador = assinatura.getDoador();
            if (doador == null) continue;

            DoadorInadimplenteDTO dto = new DoadorInadimplenteDTO();
            dto.setDoadorInadiplenteDtoId(doador.getId());
            dto.setDoadorInadiplenteDtoNome(doador.getNome());
            dto.setDoadorInadiplenteDtoEmail(doador.getEmail());
            dto.setDoadorInadiplenteDtoTelefone(doador.getTelefone());
            
            // Calcula dias em atraso baseado no último pagamento
            long diasAtraso = assinatura.getPagamentos().stream()
                .filter(p -> p.getStatus() == StatusPagamento.PAGO)
                .map(p -> p.getDataPagamento())
                .max(Date::compareTo)
                .map(ultimoPagamento -> {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(ultimoPagamento);
                    cal.add(Calendar.MONTH, 1); // Considera atraso após 1 mês do último pagamento
                    long diffInMillies = new Date().getTime() - cal.getTime().getTime();
                    return diffInMillies / (24 * 60 * 60 * 1000); // Converte para dias
                })
                .orElse(0L);
            
            dto.setDoadorInadiplenteDtoDiasAtraso((int) diasAtraso);
            
            inadimplentes.add(dto);
        }

        return inadimplentes;
    }

    public RelatorioDTO gerarRelatorio(TipoRelatorio tipo, Date dataInicio, Date dataFim) {
        Relatorio relatorio = new Relatorio();
        relatorio.setTipo(tipo);
        relatorio.setDataInicio(dataInicio);
        relatorio.setDataFim(dataFim);
        
        // Dependendo do tipo, gera relatório específico
        switch (tipo) {
            case DOACOES:
                List<Doacao> doacoes = doacaoRepository.buscarTodos().stream()
                    .filter(d -> d.getData().after(dataInicio) && d.getData().before(dataFim))
                    .collect(Collectors.toList());
                relatorio.setDoacoes(doacoes);
                break;
            
            case ASSINATURAS:
                List<Assinatura> assinaturas = assinaturaRepository.buscarTodos().stream()
                    .filter(a -> a.getDataInicio().after(dataInicio) && 
                               (a.getStatus() == StatusAssinatura.ATIVA || 
                                a.getStatus() == StatusAssinatura.INATIVA))
                    .collect(Collectors.toList());
                relatorio.setAssinaturas(assinaturas);
                break;
        }
        
        relatorioRepository.salvar(relatorio);
        
        // Converte para DTO
        RelatorioDTO dto = new RelatorioDTO();
        dto.setRelatorioDtoId(relatorio.getId());
        dto.setRelatorioDtoTipo(relatorio.getTipo());
        dto.setRelatorioDtoDataInicio(relatorio.getDataInicio());
        dto.setRelatorioDtoDataFim(relatorio.getDataFim());
        
        return dto;
    }
}
