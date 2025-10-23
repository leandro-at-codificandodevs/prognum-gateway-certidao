package br.com.prognum.gateway_certidao.docket.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCidadesByEstadoResponse {

	private List<Cidade> cidades;

	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static final class Cidade {
		private String id;
		private String nome;
		private String url;
	}
}
