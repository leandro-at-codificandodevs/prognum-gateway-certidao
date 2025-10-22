package br.com.prognum.gateway_certidao.core.services;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.exceptions.PathParameterNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;

public class ApiGatewayServiceImpl implements ApiGatewayService {

	private JsonService jsonService;

	public ApiGatewayServiceImpl(JsonService jsonService) {
		super();
		this.jsonService = jsonService;
	}

	@Override
	public <V> V parse(Class<V> type, APIGatewayV2HTTPEvent event) throws FromJsonException {
		return jsonService.fromJson(event.getBody(), type);
	}

	@Override
	public <V> APIGatewayV2HTTPResponse build2XXResponse(int statusCode, V value) {
		try {
			String body = jsonService.toJson(value);
			return APIGatewayV2HTTPResponse.builder().withStatusCode(statusCode)
					.withHeaders(Map.of("Content-Type", "application/json")).withBody(body).build();
		} catch (ToJsonException e) {
			throw new InternalServerException(e);
		}
	}

	@Override
	public <V> APIGatewayV2HTTPResponse build4XXResponse(int statusCode, Exception e) {
		String body = e.getMessage();
		return APIGatewayV2HTTPResponse.builder().withStatusCode(statusCode)
				.withHeaders(Map.of("Content-Type", "text/simple")).withBody(body).build();
	}

	@Override
	public String getPathParameter(APIGatewayV2HTTPEvent event, String parameterName)
			throws PathParameterNotFoundException {
		Map<String, String> pathParameters = event.getPathParameters();

		if (pathParameters == null || !pathParameters.containsKey(parameterName)) {
			throw new PathParameterNotFoundException(parameterName);
		}

		return pathParameters.get(parameterName);
	}
}
