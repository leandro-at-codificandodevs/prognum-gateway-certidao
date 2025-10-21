package br.com.prognum.gateway_certidao.core.services;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;

public interface JsonService {

	<T> T fromJson(String content, Class<T> type) throws FromJsonException;

	<T> String toJson(T object) throws ToJsonException;

}