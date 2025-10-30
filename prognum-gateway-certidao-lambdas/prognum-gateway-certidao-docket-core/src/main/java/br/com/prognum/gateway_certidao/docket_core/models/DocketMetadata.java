package br.com.prognum.gateway_certidao.docket_core.models;

import java.util.Map;

import lombok.Data;

@Data
public class DocketMetadata {
	private String pedidoId;
	private Map<String, String> documentoObjectKeysByDocumentoId;
}
