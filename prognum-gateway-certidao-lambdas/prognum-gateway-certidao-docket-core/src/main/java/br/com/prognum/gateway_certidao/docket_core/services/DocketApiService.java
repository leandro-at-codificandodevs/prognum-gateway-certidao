package br.com.prognum.gateway_certidao.docket_core.services;

import br.com.prognum.gateway_certidao.docket_core.models.CreatePedidoRequest;
import br.com.prognum.gateway_certidao.docket_core.models.CreatePedidoResponse;
import br.com.prognum.gateway_certidao.docket_core.models.GetCidadesByEstadoResponse;
import br.com.prognum.gateway_certidao.docket_core.models.GetEstadosResponse;
import br.com.prognum.gateway_certidao.docket_core.models.GetPedidoStatusResponse;

public interface DocketApiService {

	CreatePedidoResponse createPedido(CreatePedidoRequest createPedidoRequest);
	GetPedidoStatusResponse getPedidoStatus(String pedidoId);
	byte[] downloadArquivo(String arquivoId);
	GetEstadosResponse getEstados();
	GetCidadesByEstadoResponse getCidadesByEstado(String estadoId);
}