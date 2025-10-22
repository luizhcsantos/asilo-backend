package br.unesp.asilobackend.dto;

import br.unesp.asilobackend.domain.enums.StatusPagamento;

import java.util.Date;

public class PagamentoDTO {

	private int pagamento_dto_id;

	private double pagamento_dto_valor;

	private Date pagamento_dto_data_emissao;

	private Date pagamento_dto_data_vencimento;

	private String pagamento_dto_codigo_pix;

	private String pagamento_dto_codigo_boleto;

	private StatusPagamento pagamento_dto_status;

}
