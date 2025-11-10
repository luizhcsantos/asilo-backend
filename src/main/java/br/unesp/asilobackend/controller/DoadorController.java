package br.unesp.asilobackend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unesp.asilobackend.dto.AssinaturaDTO;
import br.unesp.asilobackend.dto.DoacaoCreateDTO;
import br.unesp.asilobackend.dto.DoadorDTO;
import br.unesp.asilobackend.dto.PessoaFisicaRequestDTO;
import br.unesp.asilobackend.dto.PessoaFisicaResponseDTO;
import br.unesp.asilobackend.dto.PessoaJuridicaRequestDTO;
import br.unesp.asilobackend.dto.PessoaJuridicaResponseDTO;
import br.unesp.asilobackend.repository.DoadorRepository;
import br.unesp.asilobackend.service.AssinaturaService;
import br.unesp.asilobackend.service.DoacaoService;
import br.unesp.asilobackend.service.DoadorService;

@RestController
@RequestMapping("/api/doador")
public class DoadorController {

    @Autowired
    private DoadorService doadorService;
    @Autowired
    private AssinaturaService assinaturaService;
    @Autowired
    private DoacaoService doacaoService;
    @Autowired
    private DoadorRepository doadorRepository;

    @PostMapping("/pf")
    public ResponseEntity<?> cadastrarPessoaFisica(@RequestBody PessoaFisicaRequestDTO body){
        try {
            PessoaFisicaResponseDTO novoDoador = doadorService.salvarPessoaFisica(body);
            return ResponseEntity.ok(novoDoador);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/pj")
    public ResponseEntity<?> cadastrarPessoaJuridica(@RequestBody PessoaJuridicaRequestDTO body){
        try {
            PessoaJuridicaResponseDTO novoDoador = doadorService.salvarPessoaJuridica(body);
            return ResponseEntity.ok(novoDoador);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/doacao")
    public ResponseEntity<?> criarDoacao(@RequestBody DoacaoCreateDTO body) {
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) return ResponseEntity.status(403).body(Map.of("error", "Não autenticado"));
            String email = auth.getName();
            var doadorOpt = doadorRepository.buscarPorEmail(email);
            if (doadorOpt.isEmpty()) return ResponseEntity.status(403).body(Map.of("error", "Usuário não é doador"));
            boolean sucesso = doacaoService.criarDoacao(doadorOpt.get(), body);
            if (sucesso) return ResponseEntity.ok(Map.of("success", true));
            return ResponseEntity.badRequest().body(Map.of("error", "Falha ao criar doação"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/assinatura")
    public ResponseEntity<?> criarAssinatura(@RequestBody AssinaturaDTO body) {
        try {
            var auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || auth.getName() == null) return ResponseEntity.status(403).body(Map.of("error", "Não autenticado"));
            String email = auth.getName();
            var doadorOpt = doadorRepository.buscarPorEmail(email);
            if (doadorOpt.isEmpty()) return ResponseEntity.status(403).body(Map.of("error", "Usuário não é doador"));
            boolean sucesso = assinaturaService.criarAssinatura(body, doadorOpt.get().getId());
            if (sucesso) return ResponseEntity.ok(Map.of("success", true));
            return ResponseEntity.badRequest().body(Map.of("error", "Falha ao criar assinatura"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 1. Buscar o perfil do doador logado
     */
    @GetMapping("/meu-perfil")
    public ResponseEntity<?> getMeuPerfil() {
        try {
            Long doadorId = getUsuarioIdLogado();
            DoadorDTO perfil = doadorService.buscarPorId(doadorId);
            return ResponseEntity.ok(perfil);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 2. Listar o histórico de doações (pagamentos) do doador logado
     */
    @GetMapping("/minhas-doacoes")
    public ResponseEntity<?> getMinhasDoacoes() {
        try {
            Long doadorId = getUsuarioIdLogado();
            // Usa DTO simplificado para atender ao frontend
            var itens = doacaoService.listarDoacoesListItem(doadorId);
            return ResponseEntity.ok(itens);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 3. Listar as assinaturas ativas/inativas do doador logado
     */
    @GetMapping("/minhas-assinaturas")
    public ResponseEntity<?> getMinhasAssinaturas() {
        try {
            Long doadorId = getUsuarioIdLogado();
            var itens = assinaturaService.listarMinhasAssinaturasListItem(doadorId);
            return ResponseEntity.ok(itens);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 4. Cancelar uma assinatura específica do doador logado
     */

    @PostMapping("/assinatura/{idAssinatura}/cancelar")
    public ResponseEntity<?> cancelarAssinatura(@PathVariable Long idAssinatura) {
        try {
            Long usuarioId = getUsuarioIdLogado();
            boolean sucesso = assinaturaService.cancelarAssinatura(idAssinatura, usuarioId);
            return ResponseEntity.ok(Map.of("success", sucesso));
        } catch (Exception e) {
            return ResponseEntity.status(403).body(Map.of("error", e.getMessage()));
        }
    }

    private Long getUsuarioIdLogado() throws Exception {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getName() == null) {
             throw new Exception("Não autenticado");
        }
        // O SecurityFilter define o 'subject' como email do usuário.
        // Portanto precisamos buscar o doador pelo email e retornar seu ID.
        String email = auth.getName();
        var doadorOpt = doadorRepository.buscarPorEmail(email);
        if (doadorOpt.isEmpty()) {
            throw new Exception("Doador não encontrado para o email fornecido");
        }
        return doadorOpt.get().getId();
    }
}