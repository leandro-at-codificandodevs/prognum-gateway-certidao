package br.com.prognum.gateway_certidao.iac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Tags;
import software.amazon.awscdk.services.apigateway.ApiKey;
import software.amazon.awscdk.services.apigateway.IResource;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.UsagePlan;
import software.amazon.awscdk.services.apigateway.UsagePlanPerApiStage;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.lambda.eventsources.SqsEventSource;
import software.amazon.awscdk.services.s3.Bucket;
import software.amazon.awscdk.services.s3.BucketEncryption;
import software.amazon.awscdk.services.secretsmanager.Secret;
import software.amazon.awscdk.services.sqs.DeadLetterQueue;
import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.sqs.QueueEncryption;
import software.constructs.Construct;

public class MyStack extends Stack {

	private static final int API_CREATE_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS = 60;
	private static final int API_CREATE_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB = 128;
	
	private static final int API_GET_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS = 60;
	private static final int API_GET_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB = 128;

	private static final int API_GET_DOCUMENT_TYPES_LAMBDA_TIMEOUT_IN_SECS = 30;
	private static final int API_GET_DOCUMENT_TYPES_LAMBDA_MEMORY_SIZE_IN_MB = 128;

	private static final int DOCKET_CREATE_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS = 300;
	private static final int DOCKET_CREATE_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB = 128;
	
	private static final int DOCKET_GET_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS = 300;
	private static final int DOCKET_GET_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB = 128;

	private static final int ERROR_CREATE_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS = 30;
	private static final int ERROR_CREATE_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB = 128;
	
	private static final int ERROR_GET_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS = 30;
	private static final int ERROR_GET_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB = 128;

	private static final int DOCKET_CREATE_DOCUMENT_QUEUE_VISIBILITY_TIMEOUT_IN_SECS = 300;
	private static final int DOCKET_CREATE_DOCUMENT_QUEUE_BATCH_SIZE = 10;
	private static final int DOCKET_CREATE_DOCUMENT_DLQ_RETENTION_PERIOD_IN_DAYS = 14;
	private static final int DOCKET_CREATE_DOCUMENT_DLQ_MAX_RECEIVE_COUNT = 10;

	private static final int DOCKET_GET_DOCUMENT_QUEUE_VISIBILITY_TIMEOUT_IN_SECS = 300;
	private static final int DOCKET_GET_DOCUMENT_QUEUE_BATCH_SIZE = 10;
	private static final int DOCKET_GET_DOCUMENT_DLQ_RETENTION_PERIOD_IN_DAYS = 14;
	private static final int DOCKET_GET_DOCUMENT_DLQ_MAX_RECEIVE_COUNT = 10;
	
	private static final int ERROR_CREATE_DOCUMENT_DLQ_BATCH_SIZE = 10;
	private static final int ERROR_GET_DOCUMENT_DLQ_BATCH_SIZE = 10;

	public MyStack(Construct scope, Config config, StackProps props) {
		super(scope, String.format("%s-certidao-%s-stack", config.getSystem(), config.getEnvironment()), props);

		String system = config.getSystem();
		String environment = config.getEnvironment();
		String tenantId = config.getTenantId();
		String logLevel = config.getLogLevel();

		Tags.of(this).add("Environment", environment);
		Tags.of(this).add("System", system);

		String docketCreateDocumentGroupDlqId = String.format("%s-certidao-%s-docket-create-document-group-dlq.fifo",
				system, environment);
		Queue docketCreateDocumentGroupDlq = Queue.Builder.create(this, docketCreateDocumentGroupDlqId)
				.queueName(docketCreateDocumentGroupDlqId).encryption(QueueEncryption.KMS_MANAGED)
				.retentionPeriod(Duration.days(DOCKET_CREATE_DOCUMENT_DLQ_RETENTION_PERIOD_IN_DAYS))
				.contentBasedDeduplication(true).build();

		String docketCreateDocumentGroupQueueId = String.format("%s-certidao-%s-docket-create-document-group-queue.fifo",
				system, environment);
		Queue docketCreateDocumentQueue = Queue.Builder.create(this, docketCreateDocumentGroupQueueId)
				.queueName(docketCreateDocumentGroupQueueId).encryption(QueueEncryption.KMS_MANAGED)
				.visibilityTimeout(Duration.seconds(DOCKET_CREATE_DOCUMENT_QUEUE_VISIBILITY_TIMEOUT_IN_SECS)).fifo(true)
				.contentBasedDeduplication(true)
				.deadLetterQueue(DeadLetterQueue.builder().queue(docketCreateDocumentGroupDlq)
						.maxReceiveCount(DOCKET_CREATE_DOCUMENT_DLQ_MAX_RECEIVE_COUNT).build())
				.build();

		String docketGetDocumentGroupDlqId = String.format("%s-certidao-%s-docket-get-document-group-dlq.fifo",
				system, environment);
		Queue docketGetDocumentGroupDlq = Queue.Builder.create(this, docketGetDocumentGroupDlqId)
				.queueName(docketGetDocumentGroupDlqId).encryption(QueueEncryption.KMS_MANAGED)
				.retentionPeriod(Duration.days(DOCKET_GET_DOCUMENT_DLQ_RETENTION_PERIOD_IN_DAYS))
				.contentBasedDeduplication(true).build();

		String docketGetDocumentGroupQueueId = String
				.format("%s-certidao-%s-docket-get-document-group-queue.fifo", system, environment);
		Queue docketGetDocumentGroupQueue = Queue.Builder.create(this, docketGetDocumentGroupQueueId)
				.queueName(docketGetDocumentGroupQueueId).encryption(QueueEncryption.KMS_MANAGED)
				.visibilityTimeout(Duration.seconds(DOCKET_GET_DOCUMENT_QUEUE_VISIBILITY_TIMEOUT_IN_SECS))
				.fifo(true).contentBasedDeduplication(true)
				.deadLetterQueue(DeadLetterQueue.builder().queue(docketGetDocumentGroupDlq)
						.maxReceiveCount(DOCKET_GET_DOCUMENT_DLQ_MAX_RECEIVE_COUNT).build())
				.build();

		String docketApiSecretId = String.format("%s-certidao-%s-docket-api-secret", system, environment);
		/* { "login": "jaime.vicente", "senha": "!ab@2NLhwUp#yM@sVDfE" } */
		Secret docketApiSecret = Secret.Builder.create(this, docketApiSecretId).secretName(docketApiSecretId).build();

		String tenantBucketId = String.format("%s-certidao-%s-tenant-%s-bucket", system, environment.toLowerCase(),
				tenantId);
		Bucket tenantBucket = Bucket.Builder.create(this, tenantBucketId).bucketName(tenantBucketId)
				.removalPolicy(RemovalPolicy.DESTROY).encryption(BucketEncryption.S3_MANAGED).build();

		String apiCreateDocumentGroupFunctionId = String.format("%s-certidao-%s-api-create-document-group-lambda",
				system, environment);
		Function apiCreateDocumentGroupFunction = Function.Builder.create(this, apiCreateDocumentGroupFunctionId)
				.functionName(apiCreateDocumentGroupFunctionId)
				.code(getLambdaCode("prognum-gateway-certidao-api-create-document-group-lambda"))
				.handler("br.com.prognum.gateway_certidao.api_create_document_group.Handler::handleRequest")
				.runtime(Runtime.JAVA_17).memorySize(API_CREATE_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB)
				.timeout(Duration.seconds(API_CREATE_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS))
				.environment(Map.of("LOG_LEVEL", logLevel, "DOCKET_CREATE_DOCUMENT_QUEUE_URL",
						docketCreateDocumentQueue.getQueueUrl(), "TENANT_BUCKET_NAME", tenantBucketId, "TENANT_ID",
						tenantId))
				.build();
		docketCreateDocumentQueue.grantSendMessages(apiCreateDocumentGroupFunction);
		tenantBucket.grantWrite(apiCreateDocumentGroupFunction);

		String apiGetDocumentGroupFunctionId = String.format("%s-certidao-%s-api-get-document-group-lambda", system,
				environment);
		Function apiGetDocumentGroupFunction = Function.Builder.create(this, apiGetDocumentGroupFunctionId)
				.functionName(apiGetDocumentGroupFunctionId)
				.code(getLambdaCode("prognum-gateway-certidao-api-get-document-group-lambda"))
				.handler("br.com.prognum.gateway_certidao.api_get_document_group.Handler::handleRequest")
				.runtime(Runtime.JAVA_17).memorySize(API_GET_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB)
				.timeout(Duration.seconds(API_GET_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS))
				.environment(Map.of("LOG_LEVEL", logLevel, "DOCKET_GET_DOCUMENT_QUEUE_URL",
						docketGetDocumentGroupQueue.getQueueUrl(), "TENANT_BUCKET_NAME", tenantBucketId, "TENANT_ID",
						tenantId))
				.build();
		docketGetDocumentGroupQueue.grantSendMessages(apiGetDocumentGroupFunction);
		tenantBucket.grantRead(apiGetDocumentGroupFunction);

		String apiGetDocumentTypesFunctionId = String.format("%s-certidao-%s-api-get-document-types-lambda", system,
				environment);
		Function apiGetDocumentTypesFunction = Function.Builder.create(this, apiGetDocumentTypesFunctionId)
				.functionName(apiGetDocumentTypesFunctionId)
				.code(getLambdaCode("prognum-gateway-certidao-api-get-document-types-lambda"))
				.handler("br.com.prognum.gateway_certidao.api_get_document_types.Handler::handleRequest")
				.runtime(Runtime.JAVA_17).memorySize(API_GET_DOCUMENT_TYPES_LAMBDA_MEMORY_SIZE_IN_MB)
				.timeout(Duration.seconds(API_GET_DOCUMENT_TYPES_LAMBDA_TIMEOUT_IN_SECS)).environment(Map.of("LOG_LEVEL", logLevel)).build();

		String docketCreateDocumentFunctionId = String.format("%s-certidao-%s-docket-create-document-group-lambda",
				system, environment);
		Map<String, String> docketCreateDocumentGroupEnv = new HashMap<>();
		docketCreateDocumentGroupEnv.put("LOG_LEVEL", logLevel);
		docketCreateDocumentGroupEnv.put("DOCKET_API_SECRET_NAME", docketApiSecretId);
		docketCreateDocumentGroupEnv.put("DOCKET_API_AUTH_URL", config.getDocketApiAuthUrl());
		docketCreateDocumentGroupEnv.put("DOCKET_API_CREATE_PEDIDO_URL",
				config.getDocketApiCreatePedidoUrl());
		docketCreateDocumentGroupEnv.put("DOCKET_API_GET_PEDIDO_URL",
				config.getDocketApiGetPedidoUrl());
		docketCreateDocumentGroupEnv.put("DOCKET_API_DOWNLOAD_ARQUIVO_URL",
				config.getDocketApiDownloadArquivoUrl());
		docketCreateDocumentGroupEnv.put("DOCKET_API_GET_ESTADOS_URL",
				config.getDocketApiGetEstadosUrl());
		docketCreateDocumentGroupEnv.put("DOCKET_API_GET_CIDADES_BY_ESTADO_URL",
				config.getDocketApiGetCidadesByEstadoUrl());
		docketCreateDocumentGroupEnv.put("DOCKET_API_CENTRO_CUSTO_ID",
				config.getDocketApiCentroCustoId());
		docketCreateDocumentGroupEnv.put("DOCKET_API_TIPO_OPERACAO_ID",
				config.getDocketApiTipoOperacaoId());
		docketCreateDocumentGroupEnv.put("DOCKET_API_LEAD", config.getDocketApiLead());
		docketCreateDocumentGroupEnv.put("LOG_LEVEL", logLevel);
		docketCreateDocumentGroupEnv.put("LOG_LEVEL", logLevel);
		docketCreateDocumentGroupEnv.put("DOCKET_API_GRUPO_ID", config.getDocketApiGrupoId());
		Function docketCreateDocumentGroupFunction = Function.Builder.create(this, docketCreateDocumentFunctionId)
				.functionName(docketCreateDocumentFunctionId)
				.code(getLambdaCode("prognum-gateway-certidao-docket-create-document-group-lambda"))
				.handler("br.com.prognum.gateway_certidao.docket_create_document_group.Handler::handleRequest")
				.runtime(Runtime.JAVA_17).memorySize(DOCKET_CREATE_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB)
				.timeout(Duration.seconds(DOCKET_CREATE_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS))
				.environment(docketCreateDocumentGroupEnv).build();
		tenantBucket.grantWrite(docketCreateDocumentGroupFunction);
		docketApiSecret.grantRead(docketCreateDocumentGroupFunction);

		docketCreateDocumentGroupFunction.addEventSource(SqsEventSource.Builder.create(docketCreateDocumentQueue)
				.batchSize(DOCKET_CREATE_DOCUMENT_QUEUE_BATCH_SIZE).reportBatchItemFailures(true).build());

		String docketGetDocumentGroupFunctionId = String.format("%s-certidao-%s-docket-get-document-group-lambda",
				system, environment);
		Function docketGetDocumentGroupFunction = Function.Builder.create(this, docketGetDocumentGroupFunctionId)
				.functionName(docketGetDocumentGroupFunctionId)
				.code(getLambdaCode("prognum-gateway-certidao-docket-get-document-group-lambda"))
				.handler("br.com.prognum.gateway_certidao.docket_get_document_group.Handler::handleRequest")
				.runtime(Runtime.JAVA_17).memorySize(DOCKET_GET_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB)
				.timeout(Duration.seconds(DOCKET_GET_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS))
				.environment(Map.of("LOG_LEVEL", logLevel, "DOCKET_API_SECRET_NAME", docketApiSecretId,
						"DOCKET_API_AUTH_URL", config.getDocketApiAuthUrl(), "DOCKET_API_CREATE_PEDIDO_URL",
						config.getDocketApiCreatePedidoUrl(), "DOCKET_API_GET_PEDIDO_URL",
						config.getDocketApiGetPedidoUrl(), "DOCKET_API_DOWNLOAD_ARQUIVO_URL",
						config.getDocketApiDownloadArquivoUrl(), "DOCKET_API_GET_ESTADOS_URL",
						config.getDocketApiGetEstadosUrl(), "DOCKET_API_GET_CIDADES_BY_ESTADO_URL",
						config.getDocketApiGetCidadesByEstadoUrl()))
				.build();
		tenantBucket.grantReadWrite(docketGetDocumentGroupFunction);
		docketApiSecret.grantRead(docketGetDocumentGroupFunction);

		docketGetDocumentGroupFunction.addEventSource(SqsEventSource.Builder.create(docketGetDocumentGroupQueue)
				.batchSize(DOCKET_GET_DOCUMENT_QUEUE_BATCH_SIZE).reportBatchItemFailures(true).build());
		
		String errorCreateDocumentGroupFunctionId = String.format("%s-certidao-%s-error-create-document-group-lambda",
				system, environment);
		Function errorCreateDocumentGroupFunction = Function.Builder.create(this, errorCreateDocumentGroupFunctionId)
				.functionName(errorCreateDocumentGroupFunctionId)
				.code(getLambdaCode("prognum-gateway-certidao-error-create-document-group-lambda"))
				.handler("br.com.prognum.gateway_certidao.error_create_document_group.Handler::handleRequest")
				.runtime(Runtime.JAVA_17).memorySize(ERROR_CREATE_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB)
				.timeout(Duration.seconds(ERROR_CREATE_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS))
				.environment(Map.of("LOG_LEVEL", logLevel, "DOCKET_CREATE_DOCUMENT_QUEUE_URL",
						docketCreateDocumentQueue.getQueueUrl(), "TENANT_BUCKET_NAME", tenantBucketId, "TENANT_ID",
						tenantId))
				.build();
		tenantBucket.grantWrite(errorCreateDocumentGroupFunction);
		
		errorCreateDocumentGroupFunction.addEventSource(SqsEventSource.Builder.create(docketCreateDocumentGroupDlq)
				.batchSize(ERROR_CREATE_DOCUMENT_DLQ_BATCH_SIZE).reportBatchItemFailures(true).build());

		String errorGetDocumentGroupFunctionId = String.format("%s-certidao-%s-error-get-document-group-lambda", system,
				environment);
		Function errorGetDocumentGroupFunction = Function.Builder.create(this, errorGetDocumentGroupFunctionId)
				.functionName(errorGetDocumentGroupFunctionId)
				.code(getLambdaCode("prognum-gateway-certidao-error-get-document-group-lambda"))
				.handler("br.com.prognum.gateway_certidao.api_get_document_group.Handler::handleRequest")
				.runtime(Runtime.JAVA_17).memorySize(ERROR_GET_DOCUMENT_GROUP_LAMBDA_MEMORY_SIZE_IN_MB)
				.timeout(Duration.seconds(ERROR_GET_DOCUMENT_GROUP_LAMBDA_TIMEOUT_IN_SECS))
				.environment(Map.of("LOG_LEVEL", logLevel, "DOCKET_GET_DOCUMENT_QUEUE_URL",
						docketGetDocumentGroupQueue.getQueueUrl(), "TENANT_BUCKET_NAME", tenantBucketId, "TENANT_ID",
						tenantId))
				.build();
		tenantBucket.grantRead(errorGetDocumentGroupFunction);

		errorGetDocumentGroupFunction.addEventSource(SqsEventSource.Builder.create(docketGetDocumentGroupDlq)
				.batchSize(ERROR_GET_DOCUMENT_DLQ_BATCH_SIZE).reportBatchItemFailures(true).build());

		String apiId = String.format("%s-certidao-%s-api-gateway", system, environment);
		RestApi api = RestApi.Builder.create(this, apiId).restApiName(apiId).build();

		IResource apiRootResource = api.getRoot();
		Resource documentGroupsResource = apiRootResource.addResource("document-groups");
		LambdaIntegration apiCreateDocumentGroupFunctionIntegration = LambdaIntegration.Builder
				.create(apiCreateDocumentGroupFunction).build();
		documentGroupsResource.addMethod("POST", apiCreateDocumentGroupFunctionIntegration,
				MethodOptions.builder().apiKeyRequired(true).build());
		LambdaIntegration apiGetDocumentGroupFunctionIntegration = LambdaIntegration.Builder
				.create(apiGetDocumentGroupFunction).build();
		documentGroupsResource.addResource("{id}").addMethod("GET", apiGetDocumentGroupFunctionIntegration,
				MethodOptions.builder().apiKeyRequired(true).build());

		LambdaIntegration apiGetDocumentTypesFunctionIntegration = LambdaIntegration.Builder
				.create(apiGetDocumentTypesFunction).build();
		Resource documentTypesResource = apiRootResource.addResource("document-types");
		documentTypesResource.addMethod("GET", apiGetDocumentTypesFunctionIntegration,
				MethodOptions.builder().apiKeyRequired(true).build());

		String tenantApiKeyId = String.format("%s-certidao-%s-tenant-%s-api-key", system, environment, tenantId);
		ApiKey tenantApiKey = ApiKey.Builder.create(this, tenantApiKeyId).apiKeyName(tenantApiKeyId).build();
		String tenantUsagePlanId = String.format("%s-certidao-%s-tenant-%s-usage-plan", system, environment, tenantId);
		UsagePlan tenantUsagePlan = UsagePlan.Builder.create(this, tenantUsagePlanId).name(tenantUsagePlanId)
				.apiStages(List.of(UsagePlanPerApiStage.builder().api(api).stage(api.getDeploymentStage()).build()))
				.build();
		tenantUsagePlan.addApiKey(tenantApiKey);
	}

	private static Code getLambdaCode(String lambdaName) {
		return Code.fromAsset(String.format("../prognum-gateway-certidao-lambdas/%1$s/target/%1$s.jar", lambdaName));
	}
}
