package br.unesp.asilobackend.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unesp.asilobackend.domain.Doacao;
import br.unesp.asilobackend.domain.Pagamento;
import br.unesp.asilobackend.domain.enums.MeioPagamento;
import br.unesp.asilobackend.domain.enums.StatusPagamento;
import br.unesp.asilobackend.dto.PagamentoDTO;
import br.unesp.asilobackend.repository.DoacaoRepository;
import br.unesp.asilobackend.repository.PagamentoRepository;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private DoacaoRepository doacaoRepository;

    @Autowired
    private AssinaturaService assinaturaService;

    public Pagamento gerarPagamento(Doacao doacao, double valor) {
        if (doacao == null || doacao.getDoador() == null) {
            throw new IllegalArgumentException("Doação ou doador inválidos");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setData(new Date());
        pagamento.setValor(valor);
        pagamento.setMeioPagamento(MeioPagamento.PIX);
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setDoacao(doacao);

        return pagamentoRepository.salvar(pagamento);
    }

    public boolean confirmarPagamento(Long idPagamento) {
        Optional<Pagamento> optPagamento = pagamentoRepository.buscarPorId(idPagamento);
        
        if (optPagamento.isEmpty()) {
            return false;
        }

        Pagamento pagamento = optPagamento.get();
        if (pagamento.getStatus() != StatusPagamento.PENDENTE) {
            return false;
        }

        pagamento.setStatus(StatusPagamento.PAGO);
        pagamento.setDataPagamento(new Date());
        
        pagamentoRepository.salvar(pagamento);
        
        return true;
    }

    public Optional<Pagamento> reemitirBoleto(Long idPagamento) {
        Optional<Pagamento> optPagamento = pagamentoRepository.buscarPorId(idPagamento);
        
        if (optPagamento.isEmpty() || 
            optPagamento.get().getMeioPagamento() != MeioPagamento.BOLETO ||
            optPagamento.get().getStatus() != StatusPagamento.PENDENTE) {
            return Optional.empty();
        }

        Pagamento pagamento = optPagamento.get();
        pagamento.setDataVencimento(new Date(System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000))); // +3 dias
        return Optional.of(pagamentoRepository.salvar(pagamento));
    }

    public Optional<String> gerarPix(Long idPagamento) {
        Optional<Pagamento> optPagamento = pagamentoRepository.buscarPorId(idPagamento);
        
        if (optPagamento.isEmpty() || 
            optPagamento.get().getMeioPagamento() != MeioPagamento.PIX ||
            optPagamento.get().getStatus() != StatusPagamento.PENDENTE) {
            return Optional.empty();
        }

        // TODO: Integração com API de PIX
        // Por enquanto retorna um código PIX fake
        return Optional.of("00020126580014br.gov.bcb.pix0136123e4567-e12b-12d1-a456-426655440000" + 
                         "5204000053039865802BR5913Asilo Exemplo6008Sao Paulo62070503***63041234");
    }

    public Optional<PagamentoDTO> buscarPagamentoPorDoacao(Long idDoacao) {
        Optional<Doacao> optDoacao = doacaoRepository.buscarPorId(idDoacao);
        if (optDoacao.isEmpty() || optDoacao.get().getPagamento() == null) {
            return Optional.empty();
        }

        Pagamento pagamento = optDoacao.get().getPagamento();
        PagamentoDTO dto = new PagamentoDTO();
        dto.setPagamentoDtoId(pagamento.getId());
        dto.setPagamentoDtoValor(pagamento.getValor());
        dto.setPagamentoDtoStatus(pagamento.getStatus());
        dto.setPagamentoDtoMeioPagamento(pagamento.getMeioPagamento());
        dto.setPagamentoDtoData(pagamento.getData());
        dto.setPagamentoDtoDataPagamento(pagamento.getDataPagamento());
        dto.setPagamentoDtoDataVencimento(pagamento.getDataVencimento());

        return Optional.of(dto);
    }

    public void processarFalhaPagamento(Long idPagamento) {
        Optional<Pagamento> optPagamento = pagamentoRepository.buscarPorId(idPagamento);
        if (optPagamento.isEmpty()) {
            return;
        }

        Pagamento pagamento = optPagamento.get();
        if (pagamento.getStatus() != StatusPagamento.PENDENTE) {
            return;
        }

        pagamento.setStatus(StatusPagamento.FALHA);
        pagamentoRepository.salvar(pagamento);

        // Se for um pagamento de assinatura, notifica o AssinaturaService
        assinaturaService.processarFalhaPagamento(pagamento);
    }
}
