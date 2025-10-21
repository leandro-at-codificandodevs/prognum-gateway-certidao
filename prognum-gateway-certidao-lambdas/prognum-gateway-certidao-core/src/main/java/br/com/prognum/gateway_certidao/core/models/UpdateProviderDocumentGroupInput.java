package br.com.prognum.gateway_certidao.core.models;

import lombok.Data;

@Data
public class UpdateProviderDocumentGroupInput {
	private String bucketName;
	private String documentGroupObjectKey;
}
