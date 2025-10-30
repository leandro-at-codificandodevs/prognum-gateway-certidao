package br.com.prognum.gateway_certidao.docket_core.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePedidoResponse {
	private Pedido pedido;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Pedido {
		private String id;
		private String status;
		private List<Documento> documentos;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Documento {
		private String id;
		private String status;
		private String pedidoId;
	}
}
