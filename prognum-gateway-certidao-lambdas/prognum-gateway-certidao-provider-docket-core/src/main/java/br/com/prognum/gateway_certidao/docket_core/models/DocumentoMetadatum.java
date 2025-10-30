package br.com.prognum.gateway_certidao.docket_core.models;

import lombok.Data;

@Data
public class DocumentoMetadatum {
	private String documentKitId;
	private String produtoId;
	private String kitId;
	private String kitNome;
	private String titularTipo;
	private String documentoNome;
	private String documentTypeId;
}
