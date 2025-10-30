package br.com.prognum.gateway_certidao.api_get_document_group;

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
import br.com.prognum.gateway_certidao.core.services.ULIDService;
import br.com.prognum.gateway_certidao.core.services.ULIDServiceImpl;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;

public class Handler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

	private ApiGatewayService apiGatewayService;
	private DocumentGroupService documentGroupService;
	private QueueService queueService;

	private static final String TENANT_BUCKET_NAME = System.getenv("TENANT_BUCKET_NAME");
	private static final String DOCKET_GET_DOCUMENT_QUEUE_URL = System.getenv("DOCKET_GET_DOCUMENT_QUEUE_URL");
	
	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	public Handler() {
		JsonService jsonService = new JsonServiceImpl();

		S3Client s3Client = S3Client.builder().build();
		S3Presigner s3Presigner = S3Presigner.builder().build();
		BucketService bucketService = new BucketServiceImpl(s3Client, s3Presigner, jsonService);
		ULIDService ulidService = new ULIDServiceImpl();
		this.documentGroupService = new DocumentGroupServiceImpl(bucketService, ulidService);
		this.apiGatewayService = new ApiGatewayServiceImpl(jsonService);
		
		SqsClient sqsClient = SqsClient.builder().build();
		this.queueService = new QueueServiceImpl(sqsClient, jsonService);
	}

	@Override
	public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
		logger.info("Trantando evento {} {}", event, context);
		try {
			String documentGroupId = apiGatewayService.getPathParameter(event, "id");
			DocumentGroup documentGroup = documentGroupService.getDocumentGroupById(TENANT_BUCKET_NAME,
					documentGroupId);
			if (documentGroup.getStatus().equals(DocumentGroupStatus.PREPARING)) {
				UpdateProviderDocumentGroupInput updateProviderDocumentGroupInput = new UpdateProviderDocumentGroupInput();
				updateProviderDocumentGroupInput.setBucketName(TENANT_BUCKET_NAME);
				updateProviderDocumentGroupInput.setDocumentGroupId(documentGroupId);
				updateProviderDocumentGroupInput.setDocumentGroupObjectKey(
					documentGroupService.getDocumentGroupObjectKey(documentGroupId));
				queueService.sendMessage(DOCKET_GET_DOCUMENT_QUEUE_URL, updateProviderDocumentGroupInput, documentGroupId);
			}
			APIGatewayV2HTTPResponse response = apiGatewayService.build2XXResponse(HttpStatusCode.OK, documentGroup);
			logger.info("Evento tratado {}", response);
			return response;
		} catch (PathParameterNotFoundException e) {
			logger.error("Parâmetro de caminho não encontrado", e);
			APIGatewayV2HTTPResponse response = apiGatewayService.build4XXResponse(HttpStatusCode.BAD_REQUEST, e);
			logger.info("Evento tratado {}", response);
			return response;
		} catch (DocumentGroupNotFoundException e) {
			logger.error("Grupo de documento não encontrado", e);
			APIGatewayV2HTTPResponse response = apiGatewayService.build4XXResponse(HttpStatusCode.NOT_FOUND, e);
			logger.info("Evento tratado {}", response);
			return response;
		}
	}
}
