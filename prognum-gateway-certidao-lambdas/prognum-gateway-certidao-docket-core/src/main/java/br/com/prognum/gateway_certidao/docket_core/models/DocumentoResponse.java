package br.com.prognum.gateway_certidao.docket_core.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentoResponse {
    private String id;
    private String pedidoId;
    private DocumentoStatus status;
    private List<ArquivoResponse> arquivos;
}
