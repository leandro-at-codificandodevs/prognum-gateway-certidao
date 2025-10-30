package br.com.prognum.gateway_certidao.error_get_document_group;

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
import br.com.prognum.gateway_certidao.core.services.DocumentGroupServiceImpl;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import br.com.prognum.gateway_certidao.core.services.QueueService;
import br.com.prognum.gateway_certidao.core.services.QueueServiceImpl;
import br.com.prognum.gateway_certidao.core.services.ULIDService;
import br.com.prognum.gateway_certidao.core.services.ULIDServiceImpl;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;

public class Handler implements RequestHandler<SQSEvent, SQSBatchResponse> {

	private QueueService queueService;
	private DocumentGroupService documentGroupService;

	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	public Handler() {
		JsonService jsonService = new JsonServiceImpl();

		S3Client s3Client = S3Client.builder().build();
		S3Presigner s3Presigner = S3Presigner.builder().build();
		BucketService bucketService = new BucketServiceImpl(s3Client, s3Presigner, jsonService);
		ULIDService ulidService = new ULIDServiceImpl();
		this.documentGroupService = new DocumentGroupServiceImpl(bucketService, ulidService);

		SqsClient sqsClient = SqsClient.builder().build();
		this.queueService = new QueueServiceImpl(sqsClient, jsonService);
	}

	@Override
	public SQSBatchResponse handleRequest(SQSEvent event, Context context) {
		logger.info("Trantando evento {} {}", event, context);
		List<SQSMessage> failedMessages = new LinkedList<>();
		for (SQSMessage message : event.getRecords()) {
			try {
				UpdateProviderDocumentGroupInput updateProviderDocumentGroupInput = queueService
						.parseMessage(UpdateProviderDocumentGroupInput.class, message);
				String bucketName = updateProviderDocumentGroupInput.getBucketName();
				String documentGroupId = updateProviderDocumentGroupInput.getDocumentGroupId();
				documentGroupService.createDocumentGroupFailure(
					bucketName,
					documentGroupId);
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
