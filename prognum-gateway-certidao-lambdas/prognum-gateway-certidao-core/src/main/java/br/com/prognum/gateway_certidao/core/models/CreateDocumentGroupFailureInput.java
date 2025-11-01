package br.com.prognum.gateway_certidao.core.models;

import java.time.Instant;

import lombok.Data;

@Data
public class CreateDocumentGroupFailureInput {
	private String bucketName;
	private String documentGroupId;
	private Instant timestamp;
}
