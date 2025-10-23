package br.com.prognum.gateway_certidao.docket_process_document;

import java.net.http.HttpClient;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import br.com.prognum.gateway_certidao.core.models.UpdateProviderDocumentGroupInput;
import br.com.prognum.gateway_certidao.core.services.BucketService;
import br.com.prognum.gateway_certidao.core.services.BucketServiceImpl;
import br.com.prognum.gateway_certidao.core.services.DocumentGroupService;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import br.com.prognum.gateway_certidao.core.services.QueueService;
import br.com.prognum.gateway_certidao.core.services.QueueServiceImpl;
import br.com.prognum.gateway_certidao.core.services.SecretService;
import br.com.prognum.gateway_certidao.core.services.SecretServiceImpl;
import br.com.prognum.gateway_certidao.docket.models.DocketMetadata;
import br.com.prognum.gateway_certidao.docket.models.GetPedidoStatusResponse;
import br.com.prognum.gateway_certidao.docket.models.GetPedidoStatusResponse.Documento;
import br.com.prognum.gateway_certidao.docket.services.DockerUserServiceImpl;
import br.com.prognum.gateway_certidao.docket.services.DocketApiService;
import br.com.prognum.gateway_certidao.docket.services.DocketApiServiceImpl;
import br.com.prognum.gateway_certidao.docket.services.DocketAuthService;
import br.com.prognum.gateway_certidao.docket.services.DocketAuthServiceImpl;
import br.com.prognum.gateway_certidao.docket.services.DocketMetadataService;
import br.com.prognum.gateway_certidao.docket.services.DocketMetadataServiceImpl;
import br.com.prognum.gateway_certidao.docket.services.DocketUserService;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.sqs.SqsClient;

public class Handler implements RequestHandler<SQSEvent, SQSBatchResponse> {
	private static final String DOCUMENTO_STATUS_ENTREGUE = "ENTREGUE";

	private QueueService queueService;
		
	private DocketApiService docketApiService;
	
	private DocketMetadataService docketMetadataService;
	
	private BucketService bucketService;
	
	private static final String DOCKET_API_AUTH_URL = System.getenv("DOCKET_API_AUTH_URL");
	private static final String DOCKET_API_CREATE_PEDIDO_URL = System.getenv("DOCKET_API_CREATE_PEDIDO_URL");
	private static final String DOCKET_API_GET_PEDIDO_URL = System.getenv("DOCKET_API_GET_PEDIDO_URL");
	private static final String DOCKET_API_DOWNLOAD_DOCUMENTO_URL = System.getenv("DOCKET_API_DOWNLOAD_DOCUMENTO_URL");
	private static final String DOCKET_API_SECRET_NAME = System.getenv("DOCKET_API_SECRET_NAME");
	
	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	public Handler() {
		JsonService jsonService = new JsonServiceImpl();

		SecretsManagerClient secretsManagerClient = SecretsManagerClient.builder().build();
		SecretService secretService = new SecretServiceImpl(secretsManagerClient, jsonService);

		DocketUserService docketUserService = new DockerUserServiceImpl(DOCKET_API_SECRET_NAME, secretService);
		
		HttpClient httpClient = HttpClient.newBuilder().build();

		DocketAuthService docketAuthService = new DocketAuthServiceImpl(DOCKET_API_AUTH_URL, httpClient, jsonService,
				docketUserService);
		
		this.docketApiService = new DocketApiServiceImpl(
			httpClient, 
			docketAuthService,
			DOCKET_API_CREATE_PEDIDO_URL, 
			DOCKET_API_GET_PEDIDO_URL, 
			DOCKET_API_DOWNLOAD_DOCUMENTO_URL,
			jsonService);
		
		S3Client s3Client = S3Client.builder().build();
		S3Presigner s3Presigner = S3Presigner.builder().build();
		this.bucketService = new BucketServiceImpl(s3Client, s3Presigner, jsonService);
		this.docketMetadataService = new DocketMetadataServiceImpl(bucketService);

		SqsClient sqsClient = SqsClient.builder().build();
		this.queueService = new QueueServiceImpl(sqsClient, jsonService);
	}

	@Override
	public SQSBatchResponse handleRequest(SQSEvent event, Context context) {
		List<SQSMessage> failedMessages = new LinkedList<>();
		for (SQSMessage message : event.getRecords()) {
			try {
				UpdateProviderDocumentGroupInput updateProviderDocumentGroupInput = queueService
						.parseMessage(UpdateProviderDocumentGroupInput.class, message);
				String bucketName = updateProviderDocumentGroupInput.getBucketName();
				String documentGroupObjectKey = updateProviderDocumentGroupInput.getDocumentGroupObjectKey();
				DocketMetadata docketMetadata = docketMetadataService.read(
						bucketName,
						documentGroupObjectKey);
				String pedidoId = docketMetadata.getPedidoId();

				GetPedidoStatusResponse getPedidoStatusResponse = docketApiService.getPedidoStatus(pedidoId);
				List<Documento> documentos = getPedidoStatusResponse.getPedido().getDocumentos();
				for (GetPedidoStatusResponse.Documento documento : documentos) {
					if (documento.getStatus().equals(DOCUMENTO_STATUS_ENTREGUE)) {
						byte[] content = docketApiService.downloadFile(documento.getArquivos().get(0).getId());
						String documentObjectKey = docketMetadata.getDocumentoObjectKeysByDocumentoId().get(documento.getId());
						String documentContentObjectKey = String.format("%s/%s", documentObjectKey, DocumentGroupService.DOCUMENT_CONTENT_FILE_NAME);
						bucketService.writeBytes(bucketName, documentContentObjectKey, "application/octet-stream", content);
					}
				}
			} catch (Exception e) {
				logger.error("Erro ao tentar processar mensagem", e);
				failedMessages.add(message);
			}
		}
		return queueService.buildBatchResponse(failedMessages);
	}
}
