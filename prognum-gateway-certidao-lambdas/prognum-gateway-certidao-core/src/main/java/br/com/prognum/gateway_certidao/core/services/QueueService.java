package br.com.prognum.gateway_certidao.core.services;

import java.util.List;

import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

public interface QueueService {

	<V> V parseMessage(Class<V> type, SQSMessage message);

	<V> void sendMessage(String queueUrl, V value, String messageGroupId);
	
	SQSBatchResponse buildBatchResponse(List<SQSMessage> failedMessages);

}