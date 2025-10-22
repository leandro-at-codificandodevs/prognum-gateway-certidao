package br.com.prognum.gateway_certidao.core.models;

import java.time.Instant;
import java.util.Set;

import lombok.Data;

@Data
public class DocumentGroupMetadata {
	private String documentGroupId;
	private Instant timestamp;
	private Set<String> documentIds;
}