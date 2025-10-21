package br.com.prognum.gateway_certidao.core.models;

import java.time.Instant;
import java.util.Map;

import lombok.Data;

@Data
public class DocumentGroupMetadata {
	private String documentGroupId;
	private Instant timestamp;
	private Map<String, String> documentIdsByTypeId;
}