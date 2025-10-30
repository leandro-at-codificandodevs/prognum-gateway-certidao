package br.com.prognum.gateway_certidao.docket_core.services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.docket_core.models.AuthenticationResponse;

public class DocketAuthServiceImpl implements DocketAuthService {

	private String docketApiAuthUrl;
	private JsonService jsonService;
	private String token;
	private HttpClient httpClient;
	private DocketUserService docketUserService;

	public DocketAuthServiceImpl(String docketApiAuthUrl, HttpClient httpClient, JsonService jsonService,
			DocketUserService docketUserService) {
		this.docketApiAuthUrl = docketApiAuthUrl;
		this.jsonService = jsonService;
		this.httpClient = httpClient;
		this.docketUserService = docketUserService;
	}

	@Override
	public String getToken() {
		if (token == null) {
			try {
				token = generateNewToken();
			} catch (ToJsonException | InterruptedException | IOException | FromJsonException e) {
				throw new InternalServerException(e);
			}
		}
		return token;
	}

	private String generateNewToken() throws ToJsonException, InterruptedException, IOException, FromJsonException {
		String jsonPayload = jsonService.toJson(docketUserService.getDocketUser());
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(docketApiAuthUrl)).timeout(Duration.ofSeconds(15))
				.header("Content-Type", "application/json").POST(BodyPublishers.ofString(jsonPayload)).build();

		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
		if (response.statusCode() != 200) {
			String message = String.format("Falha ao obter token. HTTP %s - %s", +response.statusCode(),
					response.body());
			throw new InternalServerException(message);
		}
		AuthenticationResponse authnResponse = jsonService.fromJson(response.body(), AuthenticationResponse.class);
		String token = authnResponse.getToken();
		return token;
	}

	@Override
	public void invalidateToken() {
		token = null;
	}
}
