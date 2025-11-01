package br.com.prognum.gateway_certidao.error_create_document_group;

import java.time.Instant;
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
import br.com.prognum.gateway_certidao.core.models.CreateDocumentGroupFailureInput;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import br.com.prognum.gateway_certidao.core.services.QueueService;
import br.com.prognum.gateway_certidao.core.services.QueueServiceImpl;
import software.amazon.awssdk.services.sqs.SqsClient;

public class Handler implements RequestHandler<SQSEvent, SQSBatchResponse> {

	private QueueService queueService;

	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	private static final String ERROR_QUEUE_URL = System.getenv("ERROR_QUEUE_URL");

	public Handler() {
		JsonService jsonService = new JsonServiceImpl();
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
				String bucketName = createProviderDocumentGroupInput.getBucketName();
				String documentGroupId = createProviderDocumentGroupInput.getDocumentGroupId();
				CreateDocumentGroupFailureInput createDocumentGroupFailureInput = new CreateDocumentGroupFailureInput();
				createDocumentGroupFailureInput.setBucketName(bucketName);
				createDocumentGroupFailureInput.setDocumentGroupId(documentGroupId);
				createDocumentGroupFailureInput.setTimestamp(Instant.now());
				queueService.sendMessage(ERROR_QUEUE_URL, createDocumentGroupFailureInput, documentGroupId);
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
