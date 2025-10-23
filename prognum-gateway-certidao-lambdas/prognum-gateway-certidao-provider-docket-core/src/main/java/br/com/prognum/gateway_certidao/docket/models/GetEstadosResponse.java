package br.com.prognum.gateway_certidao.docket.models;

import java.util.List;

import lombok.Data;

@Data
public class GetEstadosResponse {

	private List<Estado> estados;

	@Data
	public static final class Estado {
		private int idExibicao;
		private String id;
		private String nome;
		private String sigla;
	}
}
