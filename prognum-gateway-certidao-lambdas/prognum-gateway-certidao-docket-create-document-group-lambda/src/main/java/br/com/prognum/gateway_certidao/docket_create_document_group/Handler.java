package br.com.prognum.gateway_certidao.docket_create_document_group;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import br.com.prognum.gateway_certidao.core.models.CreateProviderDocumentGroupInput;
import br.com.prognum.gateway_certidao.core.services.BucketService;
import br.com.prognum.gateway_certidao.core.services.BucketServiceImpl;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import br.com.prognum.gateway_certidao.core.services.QueueService;
import br.com.prognum.gateway_certidao.core.services.QueueServiceImpl;
import br.com.prognum.gateway_certidao.core.services.SecretService;
import br.com.prognum.gateway_certidao.core.services.SecretServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.models.CreatePedidoRequest;
import br.com.prognum.gateway_certidao.docket_core.models.CreatePedidoResponse;
import br.com.prognum.gateway_certidao.docket_core.models.DocketMetadata;
import br.com.prognum.gateway_certidao.docket_core.services.DockerUserServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.services.DocketApiService;
import br.com.prognum.gateway_certidao.docket_core.services.DocketApiServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.services.DocketAuthService;
import br.com.prognum.gateway_certidao.docket_core.services.DocketAuthServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.services.DocketMapperService;
import br.com.prognum.gateway_certidao.docket_core.services.DocketMapperServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.services.DocketMetadataService;
import br.com.prognum.gateway_certidao.docket_core.services.DocketMetadataServiceImpl;
import br.com.prognum.gateway_certidao.docket_core.services.DocketUserService;
import br.com.prognum.gateway_certidao.docket_core.services.DocumentoMetadataService;
import br.com.prognum.gateway_certidao.docket_core.services.DocumentoMetadataServiceImpl;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.sqs.SqsClient;

public class Handler implements RequestHandler<SQSEvent, SQSBatchResponse> {

	private DocketApiService docketApiService;
	private DocketMapperService docketMapperService;
	private DocketMetadataService docketMetadataService;
	private QueueService queueService;

	private static final String DOCKET_API_AUTH_URL = System.getenv("DOCKET_API_AUTH_URL");
	private static final String DOCKET_API_CREATE_PEDIDO_URL = System.getenv("DOCKET_API_CREATE_PEDIDO_URL");
	private static final String DOCKET_API_GET_PEDIDO_URL = System.getenv("DOCKET_API_GET_PEDIDO_URL");
	private static final String DOCKET_API_DOWNLOAD_ARQUIVO_URL = System.getenv("DOCKET_API_DOWNLOAD_ARQUIVO_URL");
	private static final String DOCKET_API_GET_ESTADOS_URL = System.getenv("DOCKET_API_GET_ESTADOS_URL");
	private static final String DOCKET_API_GET_CIDADES_BY_ESTADO_URL = System
			.getenv("DOCKET_API_GET_CIDADES_BY_ESTADO_URL");
	private static final String DOCKET_API_SECRET_NAME = System.getenv("DOCKET_API_SECRET_NAME");
	private static final String DOCKET_API_CENTRO_CUSTO_ID = System.getenv("DOCKET_API_CENTRO_CUSTO_ID");
	private static final String DOCKET_API_TIPO_OPERACAO_ID = System.getenv("DOCKET_API_TIPO_OPERACAO_ID");
	private static final String DOCKET_API_LEAD = System.getenv("DOCKET_API_LEAD");
	private static final String DOCKET_API_GRUPO_ID = System.getenv("DOCKET_API_GRUPO_ID");

	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	public Handler() {
		JsonService jsonService = new JsonServiceImpl();

		SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder().build();
		SecretService secretService = new SecretServiceImpl(secretsManagerClient, jsonService);

		DocketUserService docketUserService = new DockerUserServiceImpl(DOCKET_API_SECRET_NAME, secretService);

		HttpClient httpClient = HttpClient.newBuilder().build();

		DocketAuthService docketAuthService = new DocketAuthServiceImpl(DOCKET_API_AUTH_URL, httpClient, jsonService,
				docketUserService);

		this.docketApiService = new DocketApiServiceImpl(httpClient, docketAuthService, DOCKET_API_CREATE_PEDIDO_URL,
				DOCKET_API_GET_PEDIDO_URL, DOCKET_API_DOWNLOAD_ARQUIVO_URL, DOCKET_API_GET_ESTADOS_URL,
				DOCKET_API_GET_CIDADES_BY_ESTADO_URL, jsonService);

		DocumentoMetadataService documentoMetadataService = new DocumentoMetadataServiceImpl();
		this.docketMapperService = new DocketMapperServiceImpl(docketApiService, documentoMetadataService,
				DOCKET_API_CENTRO_CUSTO_ID, DOCKET_API_TIPO_OPERACAO_ID, DOCKET_API_LEAD, DOCKET_API_GRUPO_ID);

		S3Client s3Client = S3Client.builder().build();
		S3Presigner s3Presigner = S3Presigner.builder().build();
		BucketService bucketService = new BucketServiceImpl(s3Client, s3Presigner, jsonService);
		this.docketMetadataService = new DocketMetadataServiceImpl(bucketService);

		SqsClient sqsClient = SqsClient.builder().build();
		this.queueService = new QueueServiceImpl(sqsClient, jsonService);
	}

	@Override
	public SQSBatchResponse handleRequest(SQSEvent event, Context context) {
		logger.info("Trantando evento {} {}", event, context);
		List<SQSMessage> failedMessages = new LinkedList<>();
		for (SQSMessage message : event.getRecords()) {
			try {
				CreateProviderDocumentGroupInput createProviderDocumentGroupInput = queueService
						.parseMessage(CreateProviderDocumentGroupInput.class, message);

				CreatePedidoRequest createPedidoRequest = docketMapperService
						.getCreatePedidoRequest(createProviderDocumentGroupInput);

				CreatePedidoResponse createPedidoResponse = docketApiService.createPedido(createPedidoRequest);
				String pedidoId = createPedidoResponse.getPedido().getId();

				DocketMetadata docketMetadata = new DocketMetadata();
				docketMetadata.setPedidoId(pedidoId);
				docketMetadata.setDocumentoObjectKeysByDocumentoId(new HashMap<>());

				for (int i = 0; i < createProviderDocumentGroupInput.getDocumentTypeIds().size(); i++) {
					CreatePedidoResponse.Documento responseDocumento = createPedidoResponse.getPedido().getDocumentos()
							.get(i);
					String documentTypeId = createProviderDocumentGroupInput.getDocumentTypeIds().get(i);
					String documentObjectKey = createProviderDocumentGroupInput.getBucketObjectKeyByTypeId()
							.get(documentTypeId);
					docketMetadata.getDocumentoObjectKeysByDocumentoId().put(responseDocumento.getId(),
							documentObjectKey);
				}

				String bucketName = createProviderDocumentGroupInput.getBucketName();
				String bucketObjectKey = createProviderDocumentGroupInput.getBucketObjetKey();
				docketMetadataService.write(bucketName, bucketObjectKey, docketMetadata);
			} catch (Exception e) {
				logger.error("Erro ao tentar processar mensagem", e);
				failedMessages.add(message);
			}
		}
		SQSBatchResponse response = queueService.buildBatchResponse(failedMessages);
		logger.info("Evento tratado {}", response);
		return response;
	}
}
