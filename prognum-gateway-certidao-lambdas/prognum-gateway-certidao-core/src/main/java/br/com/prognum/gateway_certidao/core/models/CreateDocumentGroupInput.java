package br.com.prognum.gateway_certidao.core.models;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CreateDocumentGroupInput {
	@JsonProperty("document-type-ids")
	private List<String> documentTypeIds;
	private Map<String, String> fields;
}
