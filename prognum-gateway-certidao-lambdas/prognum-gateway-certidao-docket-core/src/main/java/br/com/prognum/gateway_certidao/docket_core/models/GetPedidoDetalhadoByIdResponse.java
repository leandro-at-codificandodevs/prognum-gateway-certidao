package br.com.prognum.gateway_certidao.docket_core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPedidoDetalhadoByIdResponse {

    private Meta meta;

    private PedidoDetalhadoResponse pedido;
}