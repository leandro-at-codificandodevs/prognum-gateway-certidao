package br.com.prognum.gateway_certidao.docket.models;

import java.util.List;

import lombok.Data;

@Data
public class GetCidadesByEstadoResponse {

	private List<Cidade> cidades;

	@Data
	public static final class Cidade {
		private String id;
		private String nome;
		private String url;
	}
}
