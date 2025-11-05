package br.unesp.asilobackend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.unesp.asilobackend.dto.AssinaturaDTO; // Você precisará criar este DTO
import br.unesp.asilobackend.dto.DoadorDTO;
import br.unesp.asilobackend.dto.PagamentoDTO;
import br.unesp.asilobackend.dto.PessoaFisicaRequestDTO;
import br.unesp.asilobackend.dto.PessoaFisicaResponseDTO;
import br.unesp.asilobackend.dto.PessoaJuridicaRequestDTO;
import br.unesp.asilobackend.dto.PessoaJuridicaResponseDTO;
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

    @PostMapping("/pf")
    public ResponseEntity<?> cadastrarPessoaFisica(@RequestBody PessoaFisicaRequestDTO body){
        try {
            // Nota: O diagrama sugere que o DoadorService
            // deveria ser renomeado ou a lógica movida, mas mantemos
            // seu DoadorService existente por enquanto.
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

    /**
     * 1. Buscar o perfil do doador logado
     */
    @GetMapping("/meu-perfil")
    public ResponseEntity<?> getMeuPerfil() {
        // A lógica de serviço pegaria o ID do usuário logado no SecurityContext
        try {
            // Exemplo:
            // Long usuarioId = SecurityContextUtils.getUsuarioId();
            // DoadorDTO perfil = doadorService.buscarPorId(usuarioId);
            // return ResponseEntity.ok(perfil);

            // Retorno Stub (substitua pela lógica real)
            DoadorDTO stubPerfil = new DoadorDTO();
            stubPerfil.setNome("Usuário Doador (Stub)");
            stubPerfil.setEmail("doador@stub.com");
            return ResponseEntity.ok(stubPerfil);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuário não encontrado: " + e.getMessage()));
        }
    }

    /**
     * 2. Listar o histórico de doações (pagamentos) do doador logado
     */
    @GetMapping("/minhas-doacoes")
    public ResponseEntity<List<PagamentoDTO>> getMinhasDoacoes() {
        // Long usuarioId = SecurityContextUtils.getUsuarioId();
        // List<PagamentoDTO> doacoes = doacaoService.listarMinhasDoacoes(usuarioId);
        // return ResponseEntity.ok(doacoes);
        return ResponseEntity.ok(List.of()); // Stub
    }

    /**
     * 3. Listar as assinaturas ativas/inativas do doador logado
     */
    @GetMapping("/minhas-assinaturas")
    public ResponseEntity<List<AssinaturaDTO>> getMinhasAssinaturas() {
        // Long usuarioId = SecurityContextUtils.getUsuarioId();
        // List<AssinaturaDTO> assinaturas = assinaturaService.listarMinhasAssinaturas(usuarioId);
        // return ResponseEntity.ok(assinaturas);
        return ResponseEntity.ok(List.of()); // Stub
    }

    /**
     * 4. Cancelar uma assinatura específica do doador logado
     */
    @PostMapping("/assinatura/{idAssinatura}/cancelar")
    public ResponseEntity<?> cancelarAssinatura(@PathVariable Long idAssinatura) {
        try {
            // Long usuarioId = SecurityContextUtils.getUsuarioId();
            // boolean sucesso = assinaturaService.cancelarAssinatura(idAssinatura, usuarioId);
            // if(sucesso) {
            //    return ResponseEntity.ok(Map.of("success", true));
            // } else {
            //    return ResponseEntity.status(403).body(Map.of("error", "Não autorizado"));
            // }
            return ResponseEntity.ok(Map.of("success", true)); // Stub
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}