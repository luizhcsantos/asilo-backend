package br.unesp.asilobackend.controller;

import br.unesp.asilobackend.domain.enums.TipoRelatorio;
import br.unesp.asilobackend.dto.AdminDTO;
import br.unesp.asilobackend.dto.RelatorioDTO;
import br.unesp.asilobackend.service.AdminService;
import br.unesp.asilobackend.service.NotificaoService;
import br.unesp.asilobackend.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // Injeta os serviços que farão o trabalho real
    @Autowired
    private AdminService adminService;
    @Autowired
    private RelatorioService relatorioService;
    @Autowired
    private NotificaoService notificaoService;

    /**
     * 1. Cadastrar um novo Administrador
     */
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarAdministrador(@RequestBody AdminDTO adminDTO) {
        try {
            // Delega a lógica de criação para o AdminService
            AdminDTO novoAdmin = adminService.cadastrarAdministrador(adminDTO);
            return ResponseEntity.ok(novoAdmin);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 2. Gerar um Relatório
     */
    @GetMapping("/relatorio")
    public ResponseEntity<?> gerarRelatorio(
            @RequestParam TipoRelatorio tipo,
            @RequestParam Date dataInicio,
            @RequestParam Date dataFim) {
        try {
            // Delega a lógica de geração para o RelatorioService
            RelatorioDTO relatorio = relatorioService.gerarRelatorio(tipo, dataInicio, dataFim);
            return ResponseEntity.ok(relatorio);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 3. Enviar notificação para um Doador específico
     */
    @PostMapping("/notificacao/individual/{idDoador}")
    public ResponseEntity<?> enviarNotificacaoParaDoador(
            @PathVariable Long idDoador,
            @RequestBody Map<String, String> payload) { // Espera um JSON como {"mensagem": "..."}
        try {
            String mensagem = payload.get("mensagem");
            boolean sucesso = notificaoService.enviarNotificacaoParaDoador(idDoador, mensagem);
            return ResponseEntity.ok(Map.of("success", sucesso));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 4. Enviar notificação em massa para todos os Doadores
     */
    @PostMapping("/notificacao/massa")
    public ResponseEntity<?> enviarNotificacaoEmMassa(
            @RequestBody Map<String, String> payload) { // Espera um JSON como {"mensagem": "..."}
        try {
            String mensagem = payload.get("mensagem");
            boolean sucesso = notificaoService.enviarNotificacaoEmMassa(mensagem);
            return ResponseEntity.ok(Map.of("success", sucesso));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}