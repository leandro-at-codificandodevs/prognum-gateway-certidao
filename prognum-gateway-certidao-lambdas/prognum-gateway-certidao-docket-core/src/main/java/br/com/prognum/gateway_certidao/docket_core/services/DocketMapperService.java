package br.com.prognum.gateway_certidao.docket_core.services;

import br.com.prognum.gateway_certidao.core.models.CreateProviderDocumentGroupInput;
import br.com.prognum.gateway_certidao.docket_core.models.CreatePedidoRequest;

public interface DocketMapperService {

	CreatePedidoRequest getCreatePedidoRequest(CreateProviderDocumentGroupInput createProviderDocumentGroupInput);

}