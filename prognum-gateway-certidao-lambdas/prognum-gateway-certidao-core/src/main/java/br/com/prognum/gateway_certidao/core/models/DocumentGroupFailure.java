package br.com.prognum.gateway_certidao.core.models;

import java.time.Instant;

import lombok.Data;

@Data
public class DocumentGroupFailure {
	private String documentGroupId;
	private Instant timestamp;
}
