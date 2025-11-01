package br.com.prognum.gateway_certidao.core.models;

import lombok.Data;

@Data
public class CreateDocumentGroupFailureInput {
	private String bucketName;
	private String documentGroupId;
}
