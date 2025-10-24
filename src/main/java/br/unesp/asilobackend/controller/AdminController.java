package br.unesp.asilobackend.controller;

import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.dto.DoacaoDetailDTO;
import br.unesp.asilobackend.service.AdminService;
import br.unesp.asilobackend.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    public ResponseEntity<List<Doador>> getDoadores() {
        List<Doador> doadores = adminService.listarDoadores();
        return ResponseEntity.ok(doadores);
    }

	public void consultarInadiplentes() {

	}

	public List<DoacaoDetailDTO> consultarDoacoes() {
		return null;
	}

	public void vizualizarIndicadores() {

	}

}
