package br.com.prognum.gateway_certidao.docket_core.tools;

import java.net.http.HttpClient;

import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.models.DocketUser;
import br.com.prognum.gateway_certidao.docket_core.models.DocumentoResponse;
import br.com.prognum.gateway_certidao.docket_core.models.GetPedidoDetalhadoByIdResponse;
import br.com.prognum.gateway_certidao.docket_core.models.GetPedidosSimplificadosResponse;
import br.com.prognum.gateway_certidao.docket_core.models.PedidoDetalhadoResponse;
import br.com.prognum.gateway_certidao.docket_core.models.PedidoSimplificadoResponse;
import br.com.prognum.gateway_certidao.docket_core.services.DocketApiService;
import br.com.prognum.gateway_certidao.docket_core.services.DocketApiServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.services.DocketAuthService;
import br.com.prognum.gateway_certidao.docket_core.services.DocketAuthServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.services.DocketUserService;

public class DocketApiPedidosDumper {
	public static final void main(String[] args) throws Exception {
		String docketApiAuthUrl = "https://sandbox-saas.docket.com.br/api/v2/auth/login";
		DocketUserService userService = new DocketUserService() {

			@Override
			public DocketUser getDocketUser() {
				DocketUser user = new DocketUser();
				user.setUsername("jaime.vicente");
				user.setPassword("!ab@2NLhwUp#yM@sVDfE");
				return user;
			}
		};
		HttpClient httpClient = HttpClient.newBuilder().build();
		JsonService jsonService = new JsonServiceImpl();
		DocketAuthService authService = new DocketAuthServiceImpl(docketApiAuthUrl, httpClient, jsonService,
				userService);
		String docketApiCreatePedidoUrl = "https://sandbox-saas.docket.com.br/api/v2/prognum/shopping-documentos/alpha/pedidos";
		String docketApiGetPedidoByIdUrl = "https://sandbox-saas.docket.com.br/api/v2/prognum/shopping-documentos/alpha/pedidos/{pedidoId}";
		String docketApiGetPedidosUrl = "https://sandbox-saas.docket.com.br/api/v2/prognum/shopping-documentos/alpha/pedidos?pagina={pagina}";
		String docketApiDownloadArquivoUrl = "https://sandbox-saas.docket.com.br/api/v2/prognum/downloads/{arquivoId}";
		String docketApiGetEstadosUrl = "https://sandbox-saas.docket.com.br/api/v2/prognum/estados";
		String docketApiGetCidadesByEstadoUrl = "https://sandbox-saas.docket.com.br/api/v2/prognum/cidades?estadoId={estadoId}";
		DocketApiService apiService = new DocketApiServiceImpl(httpClient, authService, docketApiCreatePedidoUrl,
				docketApiGetPedidoByIdUrl, docketApiGetPedidosUrl, docketApiDownloadArquivoUrl, docketApiGetEstadosUrl,
				docketApiGetCidadesByEstadoUrl, jsonService);

		int pagina = 0;
		GetPedidosSimplificadosResponse getPedidosSimplificadosResponse = apiService.getPedidosSimplificados(pagina);
		while (getPedidosSimplificadosResponse.getMeta().getTotalRegistros() > 0) {
			for (PedidoSimplificadoResponse pedidoSimplificado : getPedidosSimplificadosResponse.getPedidos()) {
				GetPedidoDetalhadoByIdResponse getPedidoDetalhadoByIdResponse = apiService
						.getPedidoDetalhadoById(pedidoSimplificado.getId());
				PedidoDetalhadoResponse pedidoDetalhadoResponse = getPedidoDetalhadoByIdResponse.getPedido();
				for (DocumentoResponse documento : pedidoDetalhadoResponse.getDocumentos()) {
					if (!documento.getArquivos().isEmpty()) {
						System.out.println(jsonService.toJson(documento));
					}
				}
			}
			pagina++;
			getPedidosSimplificadosResponse = apiService.getPedidosSimplificados(pagina);
		}
	}
}
