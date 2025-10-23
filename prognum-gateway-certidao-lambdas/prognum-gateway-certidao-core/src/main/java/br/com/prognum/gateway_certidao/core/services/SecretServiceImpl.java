package br.com.prognum.gateway_certidao.core.services;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public class SecretServiceImpl implements SecretService {

	private SecretsManagerClient client;
	private JsonService jsonService;

	public SecretServiceImpl(SecretsManagerClient client, JsonService jsonService) {
		this.client = client;
		this.jsonService = jsonService;
	}

	@Override
	public <T> T read(String secretName, Class<T> type) {
		GetSecretValueRequest request = GetSecretValueRequest.builder().secretId(secretName).build();

		GetSecretValueResponse response = client.getSecretValue(request);

		String secretContent = response.secretString();
		T secret;
		try {
			secret = jsonService.fromJson(secretContent, type);
		} catch (FromJsonException e) {
			throw new InternalServerException(e);
		}
		return secret;
	}
}
