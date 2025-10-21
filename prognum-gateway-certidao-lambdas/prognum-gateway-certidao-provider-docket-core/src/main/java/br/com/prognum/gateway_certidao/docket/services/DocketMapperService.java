package br.com.prognum.gateway_certidao.docket.services;

import br.com.prognum.gateway_certidao.core.models.CreateProviderDocumentGroupInput;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest;

public interface DocketMapperService {

	CreatePedidoRequest getCreatePedidoRequest(CreateProviderDocumentGroupInput createProviderDocumentGroupInput);

}