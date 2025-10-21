package br.com.prognum.gateway_certidao.core.models;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Document {
	private String id;
	private DocumentStatus status;
	
	@JsonProperty("download-url")
	private String downloadUrl;
	
	private String typeId;
	private Instant timestamp;
}
