package br.com.prognum.gateway_certidao.docket_core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Cidade {
	private String id;
	private String nome;
	private String url;
}