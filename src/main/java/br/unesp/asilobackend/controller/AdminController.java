package br.unesp.asilobackend.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.unesp.asilobackend.domain.enums.TipoRelatorio;
import br.unesp.asilobackend.dto.AdminDTO;
import br.unesp.asilobackend.dto.DoadorInadimplenteDTO;
import br.unesp.asilobackend.dto.IndicadoresDTO;
import br.unesp.asilobackend.dto.RelatorioDTO;
import br.unesp.asilobackend.service.AdminService;
import br.unesp.asilobackend.service.NotificaoService;
import br.unesp.asilobackend.service.RelatorioService;

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

    /**
     * 5. Indicadores para o dashboard admin
     */
    @GetMapping("/indicadores")
    public ResponseEntity<?> indicadores() {
        try {
            IndicadoresDTO indicadores = relatorioService.consultarIndicadores();
            return ResponseEntity.ok(indicadores);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 6. Listar doadores
     */
    @GetMapping("/doadores")
    public ResponseEntity<?> listarDoadores() {
        try {
            return ResponseEntity.ok(adminService.listarDoadoresListItem());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 7. Listar inadimplentes
     */
    @GetMapping("/doadores/inadimplentes")
    public ResponseEntity<?> listarInadimplentes() {
        try {
            List<DoadorInadimplenteDTO> lista = relatorioService.consultarInadiplentes();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 8. Meu perfil (admin)
     */
    @GetMapping("/perfil")
    public ResponseEntity<?> meuPerfil() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = (String) auth.getPrincipal();
            AdminDTO dto = adminService.obterPerfil(email);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/perfil")
    public ResponseEntity<?> atualizarPerfil(@RequestBody Map<String, String> payload) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String emailAtual = (String) auth.getPrincipal();
            String novoNome = payload.get("adminDtoNome");
            String novoEmail = payload.get("adminDtoEmail");
            String senhaAtual = payload.getOrDefault("adminDtoSenhaAtual", null);
            String novaSenha = payload.getOrDefault("adminDtoNovaSenha", null);
            AdminDTO atualizado = adminService.atualizarPerfil(emailAtual, novoNome, novoEmail, senhaAtual, novaSenha);
            return ResponseEntity.ok(atualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}