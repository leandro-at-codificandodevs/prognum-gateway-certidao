package br.com.prognum.gateway_certidao.core.services;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

public class QueueServiceImpl implements QueueService {

	private SqsClient sqsClient;
	private JsonService jsonService;
	
	private static final Logger logger = LoggerFactory.getLogger(QueueServiceImpl.class);

	public QueueServiceImpl(SqsClient sqsClient, JsonService jsonService) {
		super();
		this.sqsClient = sqsClient;
		this.jsonService = jsonService;
	}
	
	@Override
	public <V> V parseMessage(Class<V> type, SQSMessage message) {
		String body = message.getBody();
		try {
			return jsonService.fromJson(body, type);
		} catch (FromJsonException e) {
			throw new InternalServerException(e);
		}
	}
	
	@Override
	public <V> void sendMessage(String queueUrl, V value, String messageGroupId) {
		try {
            String messageBody = jsonService.toJson(value);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .messageGroupId(messageGroupId)
                    .build();

            logger.debug("Enviando mensagem para a fila {}", request);
            SendMessageResponse response = sqsClient.sendMessage(request);
            logger.debug("Mensagem enviada para a fila {}", response);
        } catch (ToJsonException e) {
            throw new InternalServerException(e);
        }
	}

	@Override
	public SQSBatchResponse buildBatchResponse(List<SQSMessage> failedMessages) {
		List<SQSBatchResponse.BatchItemFailure> batchItemFailures = new LinkedList<>();
		for (SQSMessage message : failedMessages) {
			batchItemFailures.add(SQSBatchResponse.BatchItemFailure.builder().withItemIdentifier(message.getMessageId()).build());
		}
		return SQSBatchResponse.builder().withBatchItemFailures(batchItemFailures).build();
	}
}