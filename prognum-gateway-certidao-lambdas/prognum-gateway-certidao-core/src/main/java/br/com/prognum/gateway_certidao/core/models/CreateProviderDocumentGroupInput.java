package br.com.prognum.gateway_certidao.core.models;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CreateProviderDocumentGroupInput {
	private String documentGroupId;
	private List<String> documentTypeIds;
	private Map<String, String> fields;

	private String bucketName;
	private String bucketObjetKey;
	private Map<String, String> bucketObjectKeyByTypeId;
}
