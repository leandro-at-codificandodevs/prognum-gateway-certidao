package br.com.prognum.gateway_certidao.docket_core.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Meta {
	private Integer limite;
	private List<String> numeroDezPrimeirasPaginas;
	private String ordenarPor;
	private Orientacao orientacao;
	private Integer paginaAtual;
	private String searchParam;
	private Integer totalEncontrados;
	private Integer totalPaginas;
	private Integer totalRegistros;

	public static enum Orientacao {
		ASC, DESC,
	}
}