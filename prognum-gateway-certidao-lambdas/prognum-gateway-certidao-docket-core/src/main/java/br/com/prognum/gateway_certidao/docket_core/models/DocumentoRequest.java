package br.com.prognum.gateway_certidao.docket_core.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoRequest {
	private String documentKitId;
	private String produtoId;
	private String kitId;
	private String kitNome;
	private String titularTipo;
	private String documentoNome;
	Map<String, Object> campos = new HashMap<>();
}