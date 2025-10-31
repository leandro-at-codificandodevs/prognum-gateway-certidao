package br.com.prognum.gateway_certidao.docket_core.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.docket_core.models.CreatePedidoRequest;
import br.com.prognum.gateway_certidao.docket_core.models.CreatePedidoResponse;
import br.com.prognum.gateway_certidao.docket_core.models.GetCidadesByEstadoResponse;
import br.com.prognum.gateway_certidao.docket_core.models.GetEstadosResponse;
import br.com.prognum.gateway_certidao.docket_core.models.GetPedidoDetalhadoByIdResponse;
import br.com.prognum.gateway_certidao.docket_core.models.GetPedidosSimplificadosResponse;

public class DocketApiServiceImpl implements DocketApiService {

	private DocketAuthService docketAuthService;

	private String docketApiCreatePedidoUrl;
	private String docketApiGetPedidoByIdUrl;
	private String docketApiGetPedidosUrl;
	private String docketApiDownloadArquivoUrl;
	private String docketApiGetEstadosUrl;
	private String docketApiGetCidadesByEstadoUrl;

	private JsonService jsonService;

	private HttpClient httpClient;

	private static final Logger logger = LoggerFactory.getLogger(DocketApiServiceImpl.class);

	public DocketApiServiceImpl(HttpClient httpClient, DocketAuthService docketAuthService,
			String docketApiCreatePedidoUrl, String docketApiGetPedidoByIdUrl, String docketApiGetPedidosUrl,
			String docketApiDownloadArquivoUrl, String docketApiGetEstadosUrl, String docketApiGetCidadesByEstadoUrl,
			JsonService jsonService) {
		this.docketApiCreatePedidoUrl = docketApiCreatePedidoUrl;
		this.docketApiGetPedidoByIdUrl = docketApiGetPedidoByIdUrl;
		this.docketApiGetPedidosUrl = docketApiGetPedidosUrl;
		this.docketApiDownloadArquivoUrl = docketApiDownloadArquivoUrl;
		this.docketApiGetEstadosUrl = docketApiGetEstadosUrl;
		this.docketApiGetCidadesByEstadoUrl = docketApiGetCidadesByEstadoUrl;
		this.httpClient = httpClient;
		this.jsonService = jsonService;
		this.docketAuthService = docketAuthService;
	}

	@Override
	public CreatePedidoResponse createPedido(CreatePedidoRequest createPedidoRequest) {
		logger.info("Criando pedido na Docket: {}", createPedidoRequest);
		String jsonPayload;
		try {
			jsonPayload = jsonService.toJson(createPedidoRequest);
		} catch (ToJsonException e) {
			throw new InternalServerException(e);
		}

		URI uri = URI.create(docketApiCreatePedidoUrl);
		HttpRequest request = HttpRequest.newBuilder().uri(uri)
				.header("Authorization", String.format("Bearer %s", docketAuthService.getToken()))
				.header("Content-Type", "application/json").POST(BodyPublishers.ofString(jsonPayload)).build();

		CreatePedidoResponse createPedidoResponse = callAndInvalidateTokenIfNeeded(() -> {
			try {
				HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

				if (response.statusCode() != 200) {
					String message = String.format("Erro ao criar pedido: HTTP %s - %s", response.statusCode(),
							response.body());
					throw new InternalServerException(message);
				}

				return jsonService.fromJson(response.body(), CreatePedidoResponse.class);
			} catch (Exception e) {
				throw new RuntimeException("Falha ao chamar API Docket", e);
			}
		});
		logger.info("Pedido criado na Docket: {}", createPedidoResponse);
		return createPedidoResponse;
	}

	@Override
	public GetPedidoDetalhadoByIdResponse getPedidoDetalhadoById(String pedidoId) {
		logger.info("Solicitando informação de pedido na Docket: {}", pedidoId);
		URI uri = URI.create(docketApiGetPedidoByIdUrl.replace("{pedidoId}", pedidoId));
		HttpRequest request = HttpRequest.newBuilder().uri(uri)
				.header("Authorization", String.format("Bearer %s", docketAuthService.getToken())).GET().build();

		GetPedidoDetalhadoByIdResponse getPedidoByIdResponse = callAndInvalidateTokenIfNeeded(() -> {
			try {
				HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

				if (response.statusCode() != 200) {
					String message = String.format("Erro ao obter status de pedido: HTTP %s - %s",
							response.statusCode(), response.body());
					throw new InternalServerException(message);
				}

				return jsonService.fromJson(response.body(), GetPedidoDetalhadoByIdResponse.class);
			} catch (Exception e) {
				throw new RuntimeException("Falha ao chamar API Docket", e);
			}
		});
		logger.info("Informação de pedido solicitada na Docket: {}", getPedidoByIdResponse);
		return getPedidoByIdResponse;
	}

	@Override
	public GetPedidosSimplificadosResponse getPedidosSimplificados(int pagina) {
		logger.info("Solicitando informação de pedidos na Docket");
		URI uri = URI.create(docketApiGetPedidosUrl.replace("{pagina}", Integer.toString(pagina)));
		HttpRequest request = HttpRequest.newBuilder().uri(uri)
				.header("Authorization", String.format("Bearer %s", docketAuthService.getToken())).GET().build();

		GetPedidosSimplificadosResponse getPedidosResponse = callAndInvalidateTokenIfNeeded(() -> {
			try {
				HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

				if (response.statusCode() != 200) {
					String message = String.format("Erro ao obter status de pedido: HTTP %s - %s",
							response.statusCode(), response.body());
					throw new InternalServerException(message);
				}

				return jsonService.fromJson(response.body(), GetPedidosSimplificadosResponse.class);
			} catch (Exception e) {
				throw new RuntimeException("Falha ao chamar API Docket", e);
			}
		});
		logger.info("Informação de pedido solicitada na Docket: {}", getPedidosResponse);
		return getPedidosResponse;
	}

	@Override
	public byte[] downloadArquivo(String arquivoId) {
		URI uri = URI.create(docketApiDownloadArquivoUrl.replace("{arquivoId}", arquivoId));
		HttpRequest request = HttpRequest.newBuilder().uri(uri)
				.header("Authorization", String.format("Bearer %s", docketAuthService.getToken())).GET().build();

		return callAndInvalidateTokenIfNeeded(() -> {
			try {
				HttpResponse<byte[]> response = httpClient.send(request, BodyHandlers.ofByteArray());

				if (response.statusCode() != 200) {
					String message = String.format("Erro ao fazer download de arquivo: HTTP %s - %s",
							response.statusCode(), response.body());
					throw new InternalServerException(message);
				}

				return response.body();
			} catch (Exception e) {
				throw new RuntimeException("Falha ao chamar API Docket", e);
			}
		});
	}

	private <T> T callAndInvalidateTokenIfNeeded(ThrowingSupplier<T> remote) {
		try {
			return remote.get();
		} catch (Exception e) {
			if (isUnauthorizedError(e)) {
				docketAuthService.invalidateToken();
				try {
					return remote.get();
				} catch (Exception ex) {
					throw new InternalServerException(ex);
				}
			}
			throw new InternalServerException(e);
		}
	}

	boolean isUnauthorizedError(Exception e) {
		String message = e.getMessage();
		if (message == null)
			return false;
		return message.contains("401") || message.contains("Unauthorized");
	}

	@FunctionalInterface
	private static interface ThrowingSupplier<T> {
		T get() throws Exception;
	}

	@Override
	public GetEstadosResponse getEstados() {
		logger.info("Solicitando informação de Estados na Docket");
		URI uri = URI.create(docketApiGetEstadosUrl);
		HttpRequest request = HttpRequest.newBuilder().uri(uri)
				.header("Authorization", String.format("Bearer %s", docketAuthService.getToken())).GET().build();

		GetEstadosResponse getEstadosResponse = callAndInvalidateTokenIfNeeded(() -> {
			try {
				HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

				if (response.statusCode() != 200) {
					String message = String.format("Erro ao obter status de pedido: HTTP %s - %s",
							response.statusCode(), response.body());
					throw new InternalServerException(message);
				}

				return jsonService.fromJson(response.body(), GetEstadosResponse.class);
			} catch (Exception e) {
				throw new RuntimeException("Falha ao chamar API Docket", e);
			}
		});
		logger.info("Informação de Estados solicitadas na Docket {}", getEstadosResponse);
		return getEstadosResponse;
	}

	@Override
	public GetCidadesByEstadoResponse getCidadesByEstado(String estadoId) {
		logger.info("Solicitando informação de Cidades do Estado {} na Docket", estadoId);
		URI uri = URI.create(docketApiGetCidadesByEstadoUrl.replace("{estadoId}", estadoId));
		HttpRequest request = HttpRequest.newBuilder().uri(uri)
				.header("Authorization", String.format("Bearer %s", docketAuthService.getToken())).GET().build();

		GetCidadesByEstadoResponse getCidadesByEstadoResponse = callAndInvalidateTokenIfNeeded(() -> {
			try {
				HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

				if (response.statusCode() != 200) {
					String message = String.format("Erro ao obter status de pedido: HTTP %s - %s",
							response.statusCode(), response.body());
					throw new InternalServerException(message);
				}

				return jsonService.fromJson(response.body(), GetCidadesByEstadoResponse.class);
			} catch (Exception e) {
				throw new RuntimeException("Falha ao chamar API Docket", e);
			}
		});
		logger.info("Informação de Cidades do Estado solicitada na Docket {}", getCidadesByEstadoResponse);
		return getCidadesByEstadoResponse;
	}
}
