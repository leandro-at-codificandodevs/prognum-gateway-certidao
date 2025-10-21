package br.com.prognum.gateway_certidao.core.services;

public interface SecretService {
	<T> T read(String secretName, Class<T> type);
}
