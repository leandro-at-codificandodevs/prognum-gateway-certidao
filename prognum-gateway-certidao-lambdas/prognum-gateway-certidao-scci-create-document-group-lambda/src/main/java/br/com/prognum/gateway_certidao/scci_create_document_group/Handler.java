package br.com.prognum.gateway_certidao.scci_create_document_group;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;

import br.com.prognum.gateway_certidao.core.exceptions.CityNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.DocumentTypeNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.FromJsonException;
import br.com.prognum.gateway_certidao.core.exceptions.InvalidDateException;
import br.com.prognum.gateway_certidao.core.exceptions.InvalidDocumentGroupRequestException;
import br.com.prognum.gateway_certidao.core.exceptions.MissingFieldException;
import br.com.prognum.gateway_certidao.core.exceptions.StateNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.UnknownFieldException;
import br.com.prognum.gateway_certidao.core.models.CreateDocumentGroupInput;
import br.com.prognum.gateway_certidao.core.models.CreateDocumentGroupOutput;
import br.com.prognum.gateway_certidao.core.models.CreateProviderDocumentGroupInput;
import br.com.prognum.gateway_certidao.core.models.Document;
import br.com.prognum.gateway_certidao.core.models.DocumentGroup;
import br.com.prognum.gateway_certidao.core.models.DocumentStatus;
import br.com.prognum.gateway_certidao.core.models.DocumentType;
import br.com.prognum.gateway_certidao.core.models.DocumentTypes;
import br.com.prognum.gateway_certidao.core.models.FieldType;
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
import br.com.prognum.gateway_certidao.core.utils.DateUtils;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.sqs.SqsClient;

public class Handler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

	private QueueService queueService;
	private ApiGatewayService apiGatewayService;
	private DocumentGroupService documentGroupService;
	private DocumentTypes documentTypes;

	private static final String TENANT_BUCKET_NAME = System.getenv("TENANT_BUCKET_NAME");
	private static final String DOCKET_CREATE_DOCUMENT_QUEUE_URL = System.getenv("DOCKET_CREATE_DOCUMENT_QUEUE_URL");
	
	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	public Handler() {
		this.documentTypes = new DocumentTypes();

		JsonService jsonService = new JsonServiceImpl();

		S3Client s3Client = S3Client.builder().build();
		S3Presigner s3Presigner = S3Presigner.builder().build();
		BucketService bucketService = new BucketServiceImpl(s3Client, s3Presigner, jsonService);
		this.documentGroupService = new DocumentGroupServiceImpl(bucketService);

		SqsClient sqsClient = SqsClient.builder().build();
		this.queueService = new QueueServiceImpl(sqsClient, jsonService);

		this.apiGatewayService = new ApiGatewayServiceImpl(jsonService);
	}

	@Override
	public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
		logger.info("Trantando evento {} {}", event, context);
		try {
			CreateDocumentGroupInput input = apiGatewayService.parse(CreateDocumentGroupInput.class, event);

			validateInput(input);

			DocumentGroup documentGroup = documentGroupService.createDocumentGroup(TENANT_BUCKET_NAME,
					input.getDocumentTypeIds());
			String documentGroupId = documentGroup.getId();

			CreateProviderDocumentGroupInput providerDocumentGroupInput = new CreateProviderDocumentGroupInput();
			providerDocumentGroupInput.setDocumentGroupId(documentGroup.getId());
			providerDocumentGroupInput
					.setBucketObjetKey(documentGroupService.getDocumentGroupObjectKey(documentGroupId));
			providerDocumentGroupInput.setDocumentTypeIds(new LinkedList<>());
			providerDocumentGroupInput.setBucketObjectKeyByTypeId(new HashMap<>());
			for (Document document : documentGroup.getDocuments()) {
				String documentTypeId = document.getTypeId();
				providerDocumentGroupInput.getDocumentTypeIds().add(documentTypeId);
				
				String documentId = document.getId();
				String bucketObjectKey = documentGroupService.getDocumentObjectKey(documentGroupId,
						documentId);
				providerDocumentGroupInput.getBucketObjectKeyByTypeId().put(documentTypeId, bucketObjectKey);
			}
			providerDocumentGroupInput.setBucketName(TENANT_BUCKET_NAME);
			providerDocumentGroupInput.setBucketObjetKey("groups/" + documentGroupId);
			providerDocumentGroupInput.setFields(input.getFields());

			queueService.sendMessage(DOCKET_CREATE_DOCUMENT_QUEUE_URL, providerDocumentGroupInput, documentGroupId);

			CreateDocumentGroupOutput output = new CreateDocumentGroupOutput();
			output.setId(documentGroupId);
			output.setStatus(DocumentStatus.PREPARING);

			APIGatewayV2HTTPResponse response = apiGatewayService.build2XXResponse(HttpStatusCode.CREATED, output);
			logger.info("Evento tratado {}", response);
			return response;
		} catch (InvalidDocumentGroupRequestException | DocumentTypeNotFoundException | UnknownFieldException
				| MissingFieldException | FromJsonException | StateNotFoundException | CityNotFoundException
				| InvalidDateException e) {
			logger.error("Erro ao tentar criar grupo de documentos", e);
			APIGatewayV2HTTPResponse response = apiGatewayService.build4XXResponse(HttpStatusCode.BAD_REQUEST, e);
			logger.info("Evento tratado {}", response);
			return response;
		}
	}

	private void validateInput(CreateDocumentGroupInput input)
			throws InvalidDocumentGroupRequestException, DocumentTypeNotFoundException, UnknownFieldException,
			MissingFieldException, StateNotFoundException, CityNotFoundException, InvalidDateException {

		if (input.getDocumentTypeIds().isEmpty()) {
			throw new InvalidDocumentGroupRequestException(
					"Lista de id de tipos de documentos (documentTypeIds) não pode ser nula ou vazia");
		}

		if (input.getFields().isEmpty()) {
			throw new InvalidDocumentGroupRequestException("Mapa de campos (fields) não pode ser nulo");
		}

		Map<String, String> inputFields = input.getFields();
		Set<String> requestFieldIds = inputFields.keySet();
		Set<String> allAcceptedFieldIds = new HashSet<>();

		List<String> documentTypeIds = input.getDocumentTypeIds();

		for (String documentTypeId : documentTypeIds) {
			DocumentType documentType = documentTypes.getDocumentTypeById(documentTypeId);
			Set<String> documentFieldIds = documentType.getFieldTypes().stream().map(FieldType::getId)
					.collect(Collectors.toSet());

			for (String key : documentFieldIds) {
				if (key.startsWith("data-")) {
					if (inputFields.get(key) == null) {
						throw new MissingFieldException(key);
					}
					DateUtils.fromScci(inputFields.get(key));
				}
			}

			allAcceptedFieldIds.addAll(documentFieldIds);
		}

		List<String> unknownFields = requestFieldIds.stream().filter(id -> !allAcceptedFieldIds.contains(id)).toList();

		if (!unknownFields.isEmpty()) {
			throw new UnknownFieldException(unknownFields.get(0));
		}

		for (String documentTypeId : documentTypeIds) {
			DocumentType documentType = documentTypes.getDocumentTypeById(documentTypeId);

			Set<String> requiredFieldIds = documentType.getFieldTypes().stream().map(FieldType::getId)
					.collect(Collectors.toSet());

			List<String> missingFields = requiredFieldIds.stream().filter(fieldId -> {
				String value = inputFields.get(fieldId);
				return value == null || value.trim().isEmpty();
			}).toList();

			if (!missingFields.isEmpty()) {
				throw new MissingFieldException(missingFields.get(0));
			}
		}
	}
}
