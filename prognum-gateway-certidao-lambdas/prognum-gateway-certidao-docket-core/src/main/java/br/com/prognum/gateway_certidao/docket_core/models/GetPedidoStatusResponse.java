package br.com.prognum.gateway_certidao.docket_core.models;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPedidoStatusResponse {

	private Pedido pedido;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Pedido {
		private String id;
		private String status;
		private String lead;
		private String centroCustoId;
		private String urlWebHookEntregaDocumento;
		private String grupoId;
		private Integer idExibicao;
		private String dataCriacao;
		private String usuarioCriacaoId;
		private String usuarioCriacaoNome;
		private String tipoOperacaoId;
		private String grupoNome;
		private List<Documento> documentos;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Documento {
		private String documentKitId;
		private String produtoId;
		private String kitId;
		private String kitNome;
		private String titularTipo;
		private String documentoNome;
		private String id;
		private String status;
		private String progresso;
		private String dataEntrega;
		private List<Arquivo> arquivos;
		private Integer idExibicao;
		private String titularId;
		private Integer titularIdExibicao;
		private String pedidoId;
		private Map<String, Object> campos;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Arquivo {
		private String nome;
		private String tipo;
		private String id;
		private List<Link> links;
		private String dataCriacao;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Link {
		private String href;
		private String nome;
	}
}
