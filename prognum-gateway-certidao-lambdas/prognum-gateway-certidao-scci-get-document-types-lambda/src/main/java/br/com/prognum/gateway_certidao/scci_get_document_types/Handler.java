package br.com.prognum.gateway_certidao.scci_get_document_types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

import br.com.prognum.gateway_certidao.core.exceptions.DocumentGroupNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.PathParameterNotFoundException;
import br.com.prognum.gateway_certidao.core.models.DocumentGroup;
import br.com.prognum.gateway_certidao.core.models.DocumentGroupStatus;
import br.com.prognum.gateway_certidao.core.models.DocumentTypes;
import br.com.prognum.gateway_certidao.core.models.UpdateProviderDocumentGroupInput;
import br.com.prognum.gateway_certidao.core.services.ApiGatewayService;
import br.com.prognum.gateway_certidao.core.services.ApiGatewayServiceImpl;
import br.com.prognum.gateway_certidao.core.services.BucketService;
import br.com.prognum.gateway_certidao.core.services.BucketServiceImpl;
import br.com.prognum.gateway_certidao.core.services.DocumentGroupService;
import br.com.prognum.gateway_certidao.core.services.DocumentGroupServiceImpl;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import br.com.prognum.gateway_certidao.core.services.QueueService;
import br.com.prognum.gateway_certidao.core.services.QueueServiceImpl;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;

public class Handler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

	private ApiGatewayService apiGatewayService;
	private JsonService jsonService;
	private DocumentTypes documentTypes;

	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	public Handler() {
		this.jsonService = new JsonServiceImpl();

		this.apiGatewayService = new ApiGatewayServiceImpl(jsonService);

		this.documentTypes = new DocumentTypes();
	}

	@Override
	public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
		logger.info("Trantando evento {} {}", event, context);
		APIGatewayV2HTTPResponse response = apiGatewayService.build2XXResponse(HttpStatusCode.OK, documentTypes);
		logger.info("Evento tratado {}", response);
		return response;

	}
}
