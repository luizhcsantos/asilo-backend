package br.unesp.asilobackend.service;

import br.unesp.asilobackend.domain.Assinatura;
import br.unesp.asilobackend.domain.Pagamento;
import br.unesp.asilobackend.domain.enums.StatusAssinatura;
import br.unesp.asilobackend.domain.enums.StatusPagamento;
import br.unesp.asilobackend.repository.AssinaturaRepository;
import br.unesp.asilobackend.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AssinaturaService {

    @Autowired
    private AssinaturaRepository assinaturaRepository;
    
    @Autowired
    private PagamentoRepository pagamentoRepository;
    
    @Autowired
    private PagamentoService pagamentoService;

    public boolean alterarValor(Long id, double novoValor) {
        if (novoValor <= 0) {
            return false;
        }

        Optional<Assinatura> optAssinatura = assinaturaRepository.buscarPorId(id);
        if (optAssinatura.isEmpty()) {
            return false;
        }

        Assinatura assinatura = optAssinatura.get();
        assinatura.setValor(novoValor);
        assinaturaRepository.salvar(assinatura);
        return true;
    }

    public boolean cancelarAssinatura(Long id) {
        Optional<Assinatura> optAssinatura = assinaturaRepository.buscarPorId(id);
        if (optAssinatura.isEmpty()) {
            return false;
        }

        Assinatura assinatura = optAssinatura.get();
        if (assinatura.getStatus() == StatusAssinatura.CANCELADA) {
            return false;
        }

        assinatura.setStatus(StatusAssinatura.CANCELADA);
        assinaturaRepository.salvar(assinatura);
        return true;
    }

    public boolean gerarCobrancaRecorrente(Long id) {
        Optional<Assinatura> optAssinatura = assinaturaRepository.buscarPorId(id);
        if (optAssinatura.isEmpty()) {
            return false;
        }

        Assinatura assinatura = optAssinatura.get();
        if (assinatura.getStatus() != StatusAssinatura.ATIVA) {
            return false;
        }

        // Cria novo pagamento para a assinatura
        Pagamento pagamento = new Pagamento();
        pagamento.setValor(assinatura.getValor());
        pagamento.setData(new Date());
        pagamento.setStatus(StatusPagamento.PENDENTE);
        pagamento.setMeioPagamento(assinatura.getMeioPagamento());
        pagamento.setAssinatura(assinatura);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 5); // 5 dias para vencimento
        pagamento.setDataVencimento(cal.getTime());

        pagamentoRepository.salvar(pagamento);
        return true;
    }

    public boolean alterarDataPagamento(Long id, int novoDia) {
        if (novoDia < 1 || novoDia > 28) { // Limitamos a 28 para evitar problemas com fevereiro
            return false;
        }

        Optional<Assinatura> optAssinatura = assinaturaRepository.buscarPorId(id);
        if (optAssinatura.isEmpty()) {
            return false;
        }

        Assinatura assinatura = optAssinatura.get();
        assinatura.setDiaPagamento(novoDia);
        assinaturaRepository.salvar(assinatura);
        return true;
    }

    public void processarFalhaPagamento(Pagamento pagamento) {
        if (pagamento == null || pagamento.getAssinatura() == null) {
            return;
        }

        Assinatura assinatura = pagamento.getAssinatura();
        
        // Conta quantos pagamentos falhos consecutivos existem
        long pagamentosFalhos = pagamentoRepository.buscarTodos().stream()
            .filter(p -> p.getAssinatura() != null && 
                    p.getAssinatura().getId().equals(assinatura.getId()) &&
                    (p.getStatus() == StatusPagamento.FALHA || p.getStatus() == StatusPagamento.VENCIDO))
            .count();

        // Se houver 3 ou mais falhas consecutivas, inativa a assinatura
        if (pagamentosFalhos >= 3) {
            assinatura.setStatus(StatusAssinatura.INADIMPLENTE);
            assinaturaRepository.salvar(assinatura);
        }
    }
}
