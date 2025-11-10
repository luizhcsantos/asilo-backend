package br.unesp.asilobackend.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.unesp.asilobackend.domain.Doacao;
import br.unesp.asilobackend.domain.Doador;
import br.unesp.asilobackend.domain.Pagamento;
import br.unesp.asilobackend.dto.DoacaoCreateDTO;
import br.unesp.asilobackend.dto.DoacaoDetailDTO;
import br.unesp.asilobackend.dto.PagamentoDTO;
import br.unesp.asilobackend.repository.DoacaoRepository;

@Service
public class DoacaoService {

	@Autowired
	private DoacaoRepository doacaoRepository;

	@Autowired
	private PagamentoService pagamentoService;

	/**
	 * Cria uma doação básica associada ao doador e salva através do repositório.
	 * Observação: o DTO atualmente não é inspecionado em detalhe aqui (projeto possui muitos métodos
	 * ainda por implementar). Esta implementação cria o objeto Doacao, associa o doador, define a data
	 * e tenta gerar um Pagamento via PagamentoService (se disponível). Retorna o resultado do salvar.
	 */
	public boolean criarDoacao(Doador doador, DoacaoCreateDTO doacao) {
		if (doador == null) {
			return false;
		}

		Doacao novo = new Doacao();
		novo.setDoador(doador);
		novo.setData(new Date());

		// Tenta gerar um pagamento (serviço pode estar incompleto; se lançar, ignora e segue)
		try {
			double valor = doacao != null ? doacao.getValor() : 0.0;
			Pagamento pagamento = pagamentoService.gerarPagamento(novo, valor);
			novo.setPagamento(pagamento);
		} catch (Exception ignored) {
		}

		// Persiste a doação (o repositório atual pode estar em desenvolvimento)
		Doacao salvo = doacaoRepository.salvar(novo);
		return salvo != null;
	}

	public DoacaoDetailDTO obterDetalhes(Long idDoacao) {
		return doacaoRepository.buscarPorId(idDoacao)
				.map(this::mapToDto)
				.orElse(null);
	}

	public List<DoacaoDetailDTO> obterDoacaoDoador(Long idDoador) {
		List<Doacao> doacoes = doacaoRepository.buscarPorDoador(idDoador);
		return doacoes.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	public List<DoacaoDetailDTO> obterTodasDoacoes() {
		List<Doacao> all = doacaoRepository.buscarTodos();
		if (all == null) return new ArrayList<>();
		return all.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	// Mapeia Doacao para DoacaoDetailDTO usando reflection para preencher campos privados do DTO
	private DoacaoDetailDTO mapToDto(Doacao d) {
		DoacaoDetailDTO dto = new DoacaoDetailDTO();
		try {
			Field fId = DoacaoDetailDTO.class.getDeclaredField("doacao_detail_dto_id_doacao");
			fId.setAccessible(true);
			fId.setInt(dto, d.getId() == null ? 0 : d.getId().intValue());

			Field fValor = DoacaoDetailDTO.class.getDeclaredField("doacao_detail_dto_valor");
			fValor.setAccessible(true);
			float valor = 0f;
			if (d.getPagamento() != null) {
				valor = (float) d.getPagamento().getValor();
			}
			fValor.setFloat(dto, valor);

			// tipo -> não disponível no domínio atualmente, deixa null
			try {
				Field fTipo = DoacaoDetailDTO.class.getDeclaredField("doacao_detail_dto_tipo");
				fTipo.setAccessible(true);
				fTipo.set(dto, null);
			} catch (NoSuchFieldException ignored) {
			}

			Field fData = DoacaoDetailDTO.class.getDeclaredField("doacao_detail_dto_data");
			fData.setAccessible(true);
			fData.set(dto, d.getData());

			Field fStatus = DoacaoDetailDTO.class.getDeclaredField("doacao_detail_dto_status");
			fStatus.setAccessible(true);
			boolean status = false;
			if (d.getPagamento() != null && d.getPagamento().getStatus() != null) {
				status = d.getPagamento().getStatus().name().equalsIgnoreCase("PAGO");
			}
			fStatus.setBoolean(dto, status);

			// meio de pagamento
			try {
				Field fMeio = DoacaoDetailDTO.class.getDeclaredField("doacao_detail_dto_meio_pagamento");
				fMeio.setAccessible(true);
				if (d.getPagamento() != null) {
					fMeio.set(dto, d.getPagamento().getMeioPagamento());
				} else {
					fMeio.set(dto, null);
				}
			} catch (NoSuchFieldException ignored) {
			}

			// anonima
			try {
				Field fAnon = DoacaoDetailDTO.class.getDeclaredField("doacao_detail_dto_anonima");
				fAnon.setAccessible(true);
				fAnon.setBoolean(dto, false);
			} catch (NoSuchFieldException ignored) {
			}

		} catch (Exception e) {
			// Se houver qualquer problema com reflection, retorna DTO parcial
		}
		return dto;
	}

    public List<PagamentoDTO> listarMinhasDoacoes(Long doadorId) {
		List<Doacao> doacoes = doacaoRepository.buscarPorDoador(doadorId);
		List<PagamentoDTO> pagamentos = new ArrayList<>();
		for (Doacao d : doacoes) {
			if (d.getPagamento() != null) {
				pagamentos.add(new PagamentoDTO(d.getPagamento().getId(), d.getPagamento().getValor()));
			}
		}
		return pagamentos;
	}



}
