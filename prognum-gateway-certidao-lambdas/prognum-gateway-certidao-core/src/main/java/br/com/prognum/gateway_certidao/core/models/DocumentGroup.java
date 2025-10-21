package br.com.prognum.gateway_certidao.core.models;

import java.time.Instant;
import java.util.List;

import lombok.Data;

@Data
public class DocumentGroup {
	private String id;
	private DocumentGroupStatus status;
	private Instant timestamp;
	private List<Document> documents;
}
