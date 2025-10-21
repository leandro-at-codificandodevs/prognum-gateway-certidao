package br.com.prognum.gateway_certidao.core.models;

import lombok.Data;

@Data
public class CreateDocumentGroupOutput {
	private String id;
	private DocumentStatus status;
}
