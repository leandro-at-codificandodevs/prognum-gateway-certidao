package br.com.prognum.gateway_certidao.docket_core.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PedidoRequest {
	private String centroCustoId;
	private String tipoOperacaoId;
	private String lead;
	private String grupoId;
	private List<DocumentoRequest> documentos = new ArrayList<>();
}