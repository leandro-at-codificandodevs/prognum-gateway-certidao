package br.com.prognum.gateway_certidao.docket_core.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PedidoDetalhadoResponse {
    private String id;
    private PedidoStatus status;
    private List<DocumentoResponse> documentos;
}