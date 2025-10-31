package br.com.prognum.gateway_certidao.docket_core.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPedidosSimplificadosResponse {

    private Meta meta;

    private List<PedidoSimplificadoResponse> pedidos;
}