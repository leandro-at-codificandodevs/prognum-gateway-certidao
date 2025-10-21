package br.com.prognum.gateway_certidao.core.models;

import java.time.Instant;

import lombok.Data;

@Data
public class DocumentMetadata {
	private String documentId;
	private String documentTypeId;
	private Instant timestamp;
}
