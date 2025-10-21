package br.com.prognum.gateway_certidao.docket.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreatePedidoRequest {
	@JsonProperty
	private Pedido pedido;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Pedido {

		@JsonProperty
		private String centroCustoId;

		@JsonProperty
		private String tipoOperacaoId;

		@JsonProperty
		private String lead;

		@JsonProperty
		private String grupoId;

		@JsonProperty
		private List<Documento> documentos = new ArrayList<>();

	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Documento {
		@JsonProperty
		private String documentKitId;

		@JsonProperty
		private String produtoId;

		@JsonProperty
		private String kitId;

		@JsonProperty
		private String kitNome;

		@JsonProperty
		private String titularTipo;

		@JsonProperty
		private String documentoNome;

		@JsonProperty
		Map<String, Object> campos = new HashMap<>();
	}
}
