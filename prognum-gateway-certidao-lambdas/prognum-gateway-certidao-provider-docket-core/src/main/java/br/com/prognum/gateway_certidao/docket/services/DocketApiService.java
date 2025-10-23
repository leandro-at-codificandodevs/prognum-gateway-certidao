package br.com.prognum.gateway_certidao.docket.services;

import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoResponse;
import br.com.prognum.gateway_certidao.docket.models.GetCidadesByEstadoResponse;
import br.com.prognum.gateway_certidao.docket.models.GetEstadosResponse;
import br.com.prognum.gateway_certidao.docket.models.GetPedidoStatusResponse;

public interface DocketApiService {

	CreatePedidoResponse createPedido(CreatePedidoRequest createPedidoRequest);
	GetPedidoStatusResponse getPedidoStatus(String pedidoId);
	byte[] downloadArquivo(String arquivoId);
	GetEstadosResponse getEstados();
	GetCidadesByEstadoResponse getCidadesByEstado(String estadoId);
}