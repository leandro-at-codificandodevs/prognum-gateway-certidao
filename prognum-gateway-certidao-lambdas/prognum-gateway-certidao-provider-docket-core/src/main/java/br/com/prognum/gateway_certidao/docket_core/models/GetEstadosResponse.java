package br.com.prognum.gateway_certidao.docket_core.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetEstadosResponse {

	private List<Estado> estados;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Estado {
		private int idExibicao;
		private String id;
		private String nome;
		private String uf;
	}
}
