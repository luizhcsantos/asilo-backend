package br.unesp.asilobackend.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.domain.Notificacao;
import br.unesp.asilobackend.domain.enums.TipoNotificacao;
import br.unesp.asilobackend.repository.DoadorRepository;

@Service
public class NotificaoService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private DoadorRepository doadorRepository;
    @Autowired
    private br.unesp.asilobackend.repository.NotificacaoRepository notificacaoRepository;

    public boolean enviarConfirmacaoEmail(String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Confirmação de Cadastro - Asilo");
            message.setText("Obrigado por se cadastrar! Por favor, confirme seu email clicando no link abaixo:\n\n" +
                          "http://localhost:8080/confirmar-email?token=" + gerarToken(email));
            mailSender.send(message);
            
            salvarNotificacao(email, TipoNotificacao.CONFIRMACAO_EMAIL, "Email de confirmação enviado");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean enviarNotificacaoVencimento(String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Aviso de Vencimento - Asilo");
            message.setText("Olá! Seu pagamento está próximo do vencimento. Por favor, evite atrasos.\n\n" +
                          "Para mais detalhes, acesse sua área do doador.");
            mailSender.send(message);
            
            salvarNotificacao(email, TipoNotificacao.VENCIMENTO, "Aviso de vencimento enviado");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean enviarNotificacaoParaDoador(Long idDoador, String mensagem) {
        Optional<Doador> optDoador = doadorRepository.buscarPorId(idDoador);
        if (optDoador.isEmpty()) {
            return false;
        }

        Doador doador = optDoador.get();
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(doador.getEmail());
            message.setSubject("Nova Mensagem - Asilo");
            message.setText(mensagem);
            mailSender.send(message);
            
            salvarNotificacao(doador.getEmail(), TipoNotificacao.PERSONALIZADA, mensagem);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean enviarNotificacaoEmMassa(String mensagem) {
        List<Doador> doadores = doadorRepository.buscarTodos();
        boolean sucesso = true;

        for (Doador doador : doadores) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(doador.getEmail());
                message.setSubject("Comunicado - Asilo");
                message.setText(mensagem);
                mailSender.send(message);
                
                salvarNotificacao(doador.getEmail(), TipoNotificacao.EM_MASSA, mensagem);
            } catch (Exception e) {
                e.printStackTrace();
                sucesso = false;
            }
        }

        return sucesso;
    }

    private String gerarToken(String email) {
        // TODO: Implementar geração segura de token
        return java.util.UUID.randomUUID().toString();
    }

    private void salvarNotificacao(String email, TipoNotificacao tipo, String mensagem) {
    Notificacao notificacao = new Notificacao();
    notificacao.setTipo(tipo);
    notificacao.setMensagem(mensagem);
    notificacao.setDataEnvio(new Date());
        // Tenta associar destinatário por email
        try {
            doadorRepository.buscarPorEmail(email).ifPresent(n -> notificacao.setDestinatario(n));
        } catch (Exception ignored) {
        }

        try {
            notificacaoRepository.salvar(notificacao);
        } catch (Exception e) {
            // Não falha o envio por conta da persistência da notificação
            e.printStackTrace();
        }
    }
}
