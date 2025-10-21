package br.com.prognum.gateway_certidao.iac;

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
import software.amazon.awscdk.services.sqs.Queue;
import software.amazon.awscdk.services.sqs.QueueEncryption;
import software.constructs.Construct;

public class MyStack extends Stack {

	private static final int DOCKET_CREATE_DOCUMENT_QUEUE_VISIBILITY_TIMEOUT_IN_SECS = 300;

	private static final int DOCKET_GET_DOCUMENT_QUEUE_VISIBILITY_TIMEOUT_IN_SECS = 300;

	public MyStack(Construct scope, Config config, StackProps props) {
		super(scope, String.format("%s-%s-stack", config.getSystem(), config.getEnvironment()), props);

		String system = config.getSystem();
		String environment = config.getEnvironment();
		String tenantId = config.getTenantId();

		Tags.of(this).add("Environment", environment);
		Tags.of(this).add("System", system);

		String docketCreateDocumentQueueId = String.format("%s-certidao-%s-provider-docket-create-document-queue.fifo",
				system, environment);
		Queue docketCreateDocumentQueue = Queue.Builder.create(this, docketCreateDocumentQueueId).queueName(docketCreateDocumentQueueId)
				.encryption(QueueEncryption.KMS_MANAGED)
				.visibilityTimeout(Duration.seconds(DOCKET_CREATE_DOCUMENT_QUEUE_VISIBILITY_TIMEOUT_IN_SECS))
				.fifo(true)
				.contentBasedDeduplication(true)
				.build();

		String docketGetDocumentQueueId = String.format("%s-certidao-%s-provider-docket-process-document-queue.fifo",
				system, environment);
		Queue docketGetDocumentQueue = Queue.Builder.create(this, docketGetDocumentQueueId).queueName(docketGetDocumentQueueId)
				.encryption(QueueEncryption.KMS_MANAGED)
				.visibilityTimeout(Duration.seconds(DOCKET_GET_DOCUMENT_QUEUE_VISIBILITY_TIMEOUT_IN_SECS))
				.fifo(true)
				.contentBasedDeduplication(true)
				.build();

		String docketApiSecretId = String.format("%s-certidao-%s-provider-docket-api-secret", system, environment);
		Secret docketApiSecret = Secret.Builder.create(this, docketApiSecretId).secretName(docketApiSecretId).build();

		String tenantBucketId = String.format("%s-certidao-%s-tenant-%s-bucket", system, environment.toLowerCase(),
				tenantId);
		Bucket tenantBucket = Bucket.Builder.create(this, tenantBucketId).bucketName(tenantBucketId)
				.removalPolicy(RemovalPolicy.DESTROY).encryption(BucketEncryption.S3_MANAGED).build();

		String scciCreateDocumentGroupFunctionId = String.format("%s-certidao-%s-scci-create-document-group-lambda",
				system, environment);
		Function scciCreateDocumentGroupFunction = Function.Builder.create(this, scciCreateDocumentGroupFunctionId)
				.functionName(scciCreateDocumentGroupFunctionId)
				.code(getLambdaCode("prognum-gateway-certidao-scci-create-document-group-lambda"))
				.handler("br.com.prognum.gateway_certidao.scci_create_document_group.Handler::handleRequest")
				.runtime(Runtime.JAVA_17).memorySize(512).timeout(Duration.seconds(30))
				.environment(
					Map.of(
						"DOCKET_CREATE_DOCUMENT_QUEUE_URL", docketCreateDocumentQueue.getQueueUrl(),
						"TENANT_BUCKET_NAME", tenantBucketId,
						"TENANT_ID", tenantId)
				).build();
		docketCreateDocumentQueue.grantSendMessages(scciCreateDocumentGroupFunction);
		tenantBucket.grantWrite(scciCreateDocumentGroupFunction);

		String scciGetDocumentGroupFunctionId = String.format("%s-certidao-%s-scci-get-document-group-lambda", system,
				environment);
		Function scciGetDocumentGroupFunction = Function.Builder.create(this, scciGetDocumentGroupFunctionId)
				.functionName(scciGetDocumentGroupFunctionId)
				.code(getLambdaCode("prognum-gateway-certidao-scci-get-document-group-lambda"))
				.handler("br.com.prognum.gateway_certidao.scci_get_document_group.Handler::handleRequest")
				.runtime(Runtime.JAVA_17).memorySize(512).timeout(Duration.seconds(30))
				.environment(
					Map.of(
						"DOCKET_GET_DOCUMENT_QUEUE_URL", docketGetDocumentQueue.getQueueUrl(),
						"TENANT_BUCKET_NAME", tenantBucketId,
						"TENANT_ID", tenantId
					)
				).build();
		docketGetDocumentQueue.grantSendMessages(scciGetDocumentGroupFunction);
		tenantBucket.grantRead(scciGetDocumentGroupFunction);

		String docketCreateDocumentFunctionId = String.format("%s-certidao-%s-provider-docket-create-document-lambda",
				system, environment);
		Function docketCreateDocumentFunction = Function.Builder.create(this, docketCreateDocumentFunctionId)
				.functionName(docketCreateDocumentFunctionId)
				.code(getLambdaCode("prognum-gateway-certidao-provider-docket-create-document-lambda"))
				.handler("br.com.prognum.gateway_certidao.docket_create_document.Handler::handleRequest")
				.runtime(Runtime.JAVA_17).memorySize(512).timeout(Duration.seconds(30))
				.environment(
					Map.of(
						"DOCKET_API_SECRET_NAME", docketApiSecretId,
						"DOCKET_API_AUTH_URL", config.getDocketApiAuthUrl(),
						"DOCKET_API_CREATE_PEDIDO_URL", config.getDocketApiCreatePedidoUrl(),
						"DOCKET_API_GET_PEDIDO_URL", config.getDocketApiGetPedidoUrl(),
						"DOCKET_API_DOWNLOAD_DOCUMENTO_URL", config.getDocketApiDownloadDocumentoUrl()
					)
				)
				.build();
		tenantBucket.grantWrite(docketCreateDocumentFunction);
		docketApiSecret.grantRead(docketCreateDocumentFunction);

		docketCreateDocumentFunction.addEventSource(
				SqsEventSource.Builder.create(docketCreateDocumentQueue).batchSize(10).reportBatchItemFailures(true).build());

		String docketProcessDocumentFunctionId = String.format("%s-certidao-%s-provider-docket-process-document-lambda",
				system, environment);
		Function docketProcessDocumentFunction = Function.Builder.create(this, docketProcessDocumentFunctionId)
				.functionName(docketProcessDocumentFunctionId)
				.code(getLambdaCode("prognum-gateway-certidao-provider-docket-process-document-lambda"))
				.handler("br.com.prognum.gateway_certidao.docket_process_document.Handler::handleRequest")
				.runtime(Runtime.JAVA_17).memorySize(512).timeout(Duration.seconds(30))
				.environment(
					Map.of(
						"DOCKET_API_SECRET_NAME", docketApiSecretId,
						"DOCKET_API_AUTH_URL", config.getDocketApiAuthUrl(),
						"DOCKET_API_CREATE_PEDIDO_URL", config.getDocketApiCreatePedidoUrl(),
						"DOCKET_API_GET_PEDIDO_URL", config.getDocketApiGetPedidoUrl(),
						"DOCKET_API_DOWNLOAD_DOCUMENTO_URL", config.getDocketApiDownloadDocumentoUrl()
					)
				)
				.build();
		tenantBucket.grantRead(docketProcessDocumentFunction);
		docketApiSecret.grantRead(docketProcessDocumentFunction);

		docketProcessDocumentFunction.addEventSource(
				SqsEventSource.Builder.create(docketGetDocumentQueue).batchSize(10).reportBatchItemFailures(true).build());


		String apiId = String.format("%s-certidao-%s-api-gateway", system, environment);
		RestApi api = RestApi.Builder.create(this, apiId).restApiName(apiId).build();

		IResource apiRootResource = api.getRoot();
		Resource documentGroupsResource = apiRootResource.addResource("document-groups");
		LambdaIntegration scciCreateDocumentGroupFunctionIntegration = LambdaIntegration.Builder
				.create(scciCreateDocumentGroupFunction).build();
		documentGroupsResource.addMethod("POST", scciCreateDocumentGroupFunctionIntegration,
				MethodOptions.builder().apiKeyRequired(true).build());
		LambdaIntegration scciGetDocumentGroupFunctionIntegration = LambdaIntegration.Builder
				.create(scciGetDocumentGroupFunction).build();
		documentGroupsResource.addResource("{id}").addMethod("GET", scciGetDocumentGroupFunctionIntegration,
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
