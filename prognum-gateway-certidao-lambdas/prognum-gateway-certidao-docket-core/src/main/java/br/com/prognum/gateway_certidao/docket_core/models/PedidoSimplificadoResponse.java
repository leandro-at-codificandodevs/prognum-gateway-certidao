package br.com.prognum.gateway_certidao.docket_core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PedidoSimplificadoResponse {
    private String id;
    private PedidoStatus status;
}