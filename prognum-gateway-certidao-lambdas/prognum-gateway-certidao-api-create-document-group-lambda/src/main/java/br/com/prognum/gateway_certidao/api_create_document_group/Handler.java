package br.com.prognum.gateway_certidao.api_create_document_group;

import java.util.HashMap;
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
import br.com.prognum.gateway_certidao.core.exceptions.MissingFieldsException;
import br.com.prognum.gateway_certidao.core.exceptions.StateNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.UnknownFieldsException;
import br.com.prognum.gateway_certidao.core.models.CreateDocumentGroupInput;
import br.com.prognum.gateway_certidao.core.models.CreateDocumentGroupOutput;
import br.com.prognum.gateway_certidao.core.models.CreateProviderDocumentGroupInput;
import br.com.prognum.gateway_certidao.core.models.Document;
import br.com.prognum.gateway_certidao.core.models.DocumentGroup;
import br.com.prognum.gateway_certidao.core.models.DocumentStatus;
import br.com.prognum.gateway_certidao.core.models.DocumentType;
import br.com.prognum.gateway_certidao.core.models.DocumentTypes;
import br.com.prognum.gateway_certidao.core.models.FieldType;
import br.com.prognum.gateway_certidao.core.models.FieldTypes;
import br.com.prognum.gateway_certidao.core.models.State;
import br.com.prognum.gateway_certidao.core.models.States;
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
import br.com.prognum.gateway_certidao.core.services.StateService;
import br.com.prognum.gateway_certidao.core.services.StateServiceImpl;
import br.com.prognum.gateway_certidao.core.services.ULIDService;
import br.com.prognum.gateway_certidao.core.services.ULIDServiceImpl;
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

	private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");
	private static final String DOCKET_CREATE_DOCUMENT_QUEUE_URL = System.getenv("DOCKET_CREATE_DOCUMENT_QUEUE_URL");

	private static final Logger logger = LoggerFactory.getLogger(Handler.class);

	public Handler() {
		StateService stateService = new StateServiceImpl();
		this.documentTypes = new DocumentTypes(stateService);

		JsonService jsonService = new JsonServiceImpl();

		S3Client s3Client = S3Client.builder().build();
		S3Presigner s3Presigner = S3Presigner.builder().build();
		BucketService bucketService = new BucketServiceImpl(s3Client, s3Presigner, jsonService);
		ULIDService ulidService = new ULIDServiceImpl();
		this.documentGroupService = new DocumentGroupServiceImpl(bucketService, ulidService);

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

			DocumentGroup documentGroup = documentGroupService.createDocumentGroup(BUCKET_NAME,
					input.getDocumentTypeIds());

			CreateProviderDocumentGroupInput providerDocumentGroupInput = buildCreateDocumentGroupInput(input,
					documentGroup);

			String documentGroupId = documentGroup.getId();

			queueService.sendMessage(DOCKET_CREATE_DOCUMENT_QUEUE_URL, providerDocumentGroupInput, documentGroupId);

			CreateDocumentGroupOutput output = buildCreateDocumentGroupOutput(documentGroupId);

			APIGatewayV2HTTPResponse response = apiGatewayService.build2XXResponse(HttpStatusCode.CREATED, output);
			logger.info("Evento tratado {}", response);
			return response;
		} catch (DocumentTypeNotFoundException | UnknownFieldsException | MissingFieldsException | FromJsonException
				| StateNotFoundException | CityNotFoundException | InvalidDateException e) {
			logger.error("Erro ao tentar criar grupo de documentos", e);
			APIGatewayV2HTTPResponse response = apiGatewayService.build4XXResponse(HttpStatusCode.BAD_REQUEST, e);
			logger.info("Evento tratado {}", response);
			return response;
		}
	}

	private CreateDocumentGroupOutput buildCreateDocumentGroupOutput(String documentGroupId) {
		CreateDocumentGroupOutput output = new CreateDocumentGroupOutput();
		output.setId(documentGroupId);
		output.setStatus(DocumentStatus.PREPARING);
		return output;
	}

	private CreateProviderDocumentGroupInput buildCreateDocumentGroupInput(CreateDocumentGroupInput input,
			DocumentGroup documentGroup) {
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
			String bucketObjectKey = documentGroupService.getDocumentObjectKey(documentGroupId, documentId);
			providerDocumentGroupInput.getBucketObjectKeyByTypeId().put(documentTypeId, bucketObjectKey);
		}
		providerDocumentGroupInput.setBucketName(BUCKET_NAME);
		providerDocumentGroupInput.setBucketObjetKey("groups/" + documentGroupId);
		providerDocumentGroupInput.setFields(input.getFields());
		return providerDocumentGroupInput;
	}

	private void validateInput(CreateDocumentGroupInput input)
			throws DocumentTypeNotFoundException, UnknownFieldsException, MissingFieldsException,
			StateNotFoundException, CityNotFoundException, InvalidDateException {
		Map<String, String> inputFields = input.getFields();

		List<String> documentTypeIds = input.getDocumentTypeIds();

		List<DocumentType> requiredDocumentTypes = new LinkedList<>();
		for (String documentTypeId : documentTypeIds) {
			DocumentType documentType = documentTypes.getDocumentTypeById(documentTypeId);
			requiredDocumentTypes.add(documentType);
		}

		Set<String> requiredFieldIds = requiredDocumentTypes.stream().flatMap(documentType -> {
			return documentType.getFieldTypes().stream().map(FieldType::getId);
		}).collect(Collectors.toSet());

		Set<String> requestedFieldIds = inputFields.keySet();

		List<String> unknownFields = requestedFieldIds.stream().filter(id -> !requiredFieldIds.contains(id)).toList();
		if (!unknownFields.isEmpty()) {
			throw new UnknownFieldsException(unknownFields);
		}

		List<String> missingFields = requiredFieldIds.stream().filter(fieldId -> !requestedFieldIds.contains(fieldId))
				.toList();
		if (!missingFields.isEmpty()) {
			throw new MissingFieldsException(missingFields);
		}

		validateStateAndCity(input);
		validateDateFields(input);
	}

	private void validateDateFields(CreateDocumentGroupInput input) throws InvalidDateException {
		Map<String, String> inputFields = input.getFields();
		Set<String> dateFieldTypeIds = Set.of(FieldTypes.DATA_CASAMENTO_FIELD_TYPE_ID,
				FieldTypes.DATA_NASCIMENTO_FIELD_TYPE_ID);
		for (String fieldId : dateFieldTypeIds) {
			String fieldValue = input.getFields().get(fieldId);
			if (fieldValue != null) {
				DateUtils.fromScci(inputFields.get(fieldId));
			}
		}
	}

	private void validateStateAndCity(CreateDocumentGroupInput input)
			throws DocumentTypeNotFoundException, CityNotFoundException, StateNotFoundException {
		String stateAcronymn = input.getFields().get("estado");
		if (stateAcronymn != null) {
			List<String> documentTypeIds = input.getDocumentTypeIds();
			for (String documentTypeId : documentTypeIds) {
				DocumentType documentType = documentTypes.getDocumentTypeById(documentTypeId);
				States states = documentType.getStates();
				State state = states.getStateByAcronymn(stateAcronymn);
				String cityName = input.getFields().get("cidade");
				if (cityName != null) {
					state.getCityByName(cityName);
				}
			}
		}
	}
}
