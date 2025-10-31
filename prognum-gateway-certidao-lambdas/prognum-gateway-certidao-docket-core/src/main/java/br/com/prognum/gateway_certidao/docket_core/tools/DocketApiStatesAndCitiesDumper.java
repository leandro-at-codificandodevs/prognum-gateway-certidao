package br.com.prognum.gateway_certidao.docket_core.tools;

import java.net.http.HttpClient;
import java.util.LinkedList;
import java.util.List;

import br.com.prognum.gateway_certidao.core.models.City;
import br.com.prognum.gateway_certidao.core.models.State;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.models.Cidade;
import br.com.prognum.gateway_certidao.docket_core.models.DocketUser;
import br.com.prognum.gateway_certidao.docket_core.models.Estado;
import br.com.prognum.gateway_certidao.docket_core.models.GetCidadesByEstadoResponse;
import br.com.prognum.gateway_certidao.docket_core.models.GetEstadosResponse;
import br.com.prognum.gateway_certidao.docket_core.services.DocketApiService;
import br.com.prognum.gateway_certidao.docket_core.services.DocketApiServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.services.DocketAuthService;
import br.com.prognum.gateway_certidao.docket_core.services.DocketAuthServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.services.DocketUserService;

public class DocketApiStatesAndCitiesDumper {

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
		GetEstadosResponse getEstadosResponse = apiService.getEstados();
		List<Estado> estados = getEstadosResponse.getEstados();
		List<State> states = new LinkedList<>();
		for (Estado estado : estados) {
			String estadoId = estado.getId();
			State state = new State();
			state.setAcronymn(estado.getUf());
			state.setCities(new LinkedList<>());
			GetCidadesByEstadoResponse cidadesByEstadoResponse = apiService.getCidadesByEstado(estadoId);
			List<Cidade> cidades = cidadesByEstadoResponse.getCidades();
			for (Cidade cidade : cidades) {
				City city = new City();
				city.setName(cidade.getNome());
				state.getCities().add(city);
			}
			states.add(state);
		}
		System.out.println(jsonService.toJson(states));
	}
}
