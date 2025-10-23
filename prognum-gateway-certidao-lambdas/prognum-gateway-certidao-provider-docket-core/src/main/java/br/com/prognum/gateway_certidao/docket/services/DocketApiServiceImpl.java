package br.com.prognum.gateway_certidao.docket.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoRequest;
import br.com.prognum.gateway_certidao.docket.models.CreatePedidoResponse;
import br.com.prognum.gateway_certidao.docket.models.GetCidadesByEstadoResponse;
import br.com.prognum.gateway_certidao.docket.models.GetEstadosResponse;
import br.com.prognum.gateway_certidao.docket.models.GetPedidoStatusResponse;

public class DocketApiServiceImpl implements DocketApiService {

	private DocketAuthService docketAuthService;
	private String docketApiCreatePedidoUrl;
	private String docketApiGetPedidoUrl;
	private String docketApiDownloadArquivoUrl;
	private String docketApiGetEstadosUrl;
	private String docketApiGetCidadesByEstadoUrl;
	private JsonService jsonService;
	private HttpClient httpClient;

	public DocketApiServiceImpl(
		HttpClient httpClient,
		DocketAuthService docketAuthService,
		String docketApiCreatePedidoUrl,
		String docketApiGetPedidoUrl,
		String docketApiDownloadArquivoUrl,
		String docketApiGetEstadosUrl,
		String docketApiGetCidadesByEstadoUrl,
		JsonService jsonService) {
		this.docketApiCreatePedidoUrl = docketApiCreatePedidoUrl;
		this.docketApiGetPedidoUrl = docketApiGetPedidoUrl;
		this.docketApiDownloadArquivoUrl = docketApiDownloadArquivoUrl;
		this.docketApiGetEstadosUrl = docketApiGetEstadosUrl;
		this.docketApiGetCidadesByEstadoUrl = docketApiGetCidadesByEstadoUrl;
		this.httpClient = httpClient;
		this.jsonService = jsonService;
		this.docketAuthService = docketAuthService;
		
		try {
			GetEstadosResponse getEstadosResponse = getEstados();
			System.out.println(jsonService.toJson(getEstadosResponse.getEstados()));
			for (GetEstadosResponse.Estado estado : getEstadosResponse.getEstados()) {
				GetCidadesByEstadoResponse getCidadesByEstadoResponse = getCidadesByEstado(estado.getId());
				System.out.println(jsonService.toJson(getCidadesByEstadoResponse.getCidades()));
			}
		} catch (ToJsonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public CreatePedidoResponse createPedido(CreatePedidoRequest createPedidoRequest) {
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

		return callAndInvalidateTokenIfNeeded(() -> {
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
	}

	public GetPedidoStatusResponse getPedidoStatus(String pedidoId) {
		URI uri = URI.create(docketApiGetPedidoUrl.replace("{pedidoId}", pedidoId));
		HttpRequest request = HttpRequest.newBuilder().uri(uri)
				.header("Authorization", String.format("Bearer %s", docketAuthService.getToken())).GET().build();

		return callAndInvalidateTokenIfNeeded(() -> {
			try {
				HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

				if (response.statusCode() != 200) {
					String message = String.format("Erro ao obter status de pedido: HTTP %s - %s",
							response.statusCode(), response.body());
					throw new InternalServerException(message);
				}

				return jsonService.fromJson(response.body(), GetPedidoStatusResponse.class);
			} catch (Exception e) {
				throw new RuntimeException("Falha ao chamar API Docket", e);
			}
		});
	}

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
		URI uri = URI.create(docketApiGetEstadosUrl);
		HttpRequest request = HttpRequest.newBuilder().uri(uri)
				.header("Authorization", String.format("Bearer %s", docketAuthService.getToken())).GET().build();

		return callAndInvalidateTokenIfNeeded(() -> {
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
	}

	@Override
	public GetCidadesByEstadoResponse getCidadesByEstado(String estadoId) {
		URI uri = URI.create(docketApiGetCidadesByEstadoUrl.replace("{estadoId}", estadoId));
		HttpRequest request = HttpRequest.newBuilder().uri(uri)
				.header("Authorization", String.format("Bearer %s", docketAuthService.getToken())).GET().build();

		return callAndInvalidateTokenIfNeeded(() -> {
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
	}
}
