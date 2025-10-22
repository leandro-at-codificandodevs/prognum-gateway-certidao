package br.com.prognum.gateway_certidao.docket.services;

import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoResponse;
import br.com.prognum.gateway_certidao.docket.models.GetPedidoStatusResponse;

public interface DocketApiService {

	CreatePedidoResponse createPedido(CreatePedidoRequest createPedidoRequest);
	GetPedidoStatusResponse getPedidoStatus(String pedidoId);
	byte[] downloadFile(String fileId);
}