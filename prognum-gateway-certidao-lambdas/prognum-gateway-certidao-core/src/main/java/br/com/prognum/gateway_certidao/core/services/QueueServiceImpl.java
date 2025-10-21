package br.com.prognum.gateway_certidao.core.services;

import java.util.LinkedList;
import java.util.List;

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

public class QueueServiceImpl implements QueueService {

	private SqsClient sqsClient;
	private JsonService jsonService;

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

            sqsClient.sendMessage(request);
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