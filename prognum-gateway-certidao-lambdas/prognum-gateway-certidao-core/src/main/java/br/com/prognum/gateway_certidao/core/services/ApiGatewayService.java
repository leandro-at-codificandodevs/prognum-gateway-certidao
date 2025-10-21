package br.com.prognum.gateway_certidao.core.services;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.PathParameterNotFoundException;

public interface ApiGatewayService {
	<V> V parse(Class<V> type, APIGatewayV2HTTPEvent event) throws FromJsonException;

	<V> APIGatewayV2HTTPResponse build2XXResponse(int statusCode, V value);

	<V> APIGatewayV2HTTPResponse build4XXResponse(int statusCode, Exception e);

	String getPathParameter(APIGatewayV2HTTPEvent event, String parameterName) throws PathParameterNotFoundException;
}
