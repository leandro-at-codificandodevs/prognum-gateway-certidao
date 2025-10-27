package br.com.prognum.gateway_certidao.core.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.com.prognum.gateway_certidao.core.exceptions.DocumentTypeNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import software.amazon.awssdk.utils.StringUtils;

public class DocumentTypes {

	public static final String DOCUMENT_TYPE_ID_1 = "cert-acoes-civis-justica-federal-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_2 = "cert-acoes-civis-justica-federal-1a-instancia-pj";
	public static final String DOCUMENT_TYPE_ID_3 = "cert-acoes-criminais-justica-federal-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_4 = "cert-acoes-criminais-justica-federal-1a-instancia-pj";
	public static final String DOCUMENT_TYPE_ID_5 = "cert-negativa-debitos-trabalhistas-pf";
	public static final String DOCUMENT_TYPE_ID_6 = "cert-negativa-debitos-trabalhistas-pj";
	public static final String DOCUMENT_TYPE_ID_7 = "cert-regularidade-fiscal-estadual-pf";
	public static final String DOCUMENT_TYPE_ID_8 = "cert-distribuicao-acoes-civeis-justica-estadual-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_9 = "cert-distribuicao-acoes-civeis-justica-estadual-1a-instancia-pj";

	private final FieldTypes fieldTypes;

	private final Map<String, DocumentType> documentTypesById;

	public DocumentTypes() {
		fieldTypes = new FieldTypes();

		documentTypesById = new HashMap<>();

		newDocumentType(DOCUMENT_TYPE_ID_1,
				"Certidão de Distribuição de Ações Cíveis - Justiça Federal - 1a instância - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_2,
				"Certidão de Distribuição de Ações Criminais - Justiça Federal - 1a instância - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_3,
				"Certidão de Distribuição de Ações Criminais - Justiça Federal - 1a instância - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_4,
				"Certidão de Distribuição de Ações Criminais - Justiça Federal - 1a instância - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_5, "Certidão Negativa de Débitos Trabalhistas - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_6, "Certidão Negativa de Débitos Trabalhistas - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_7, "Certidão de Regularidade Fiscal Estadual - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_8,
				"Certidão De Distribuição De Ações Cíveis - Justiça Estadual (1a Instância) - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.RG_FIELD_TYPE_ID,
				FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_9,
				"Certidão De Distribuição De Ações Cíveis - Justiça Estadual (1a Instância) - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);
	}

	private DocumentType newDocumentType(String id, String name, String... fieldTypeIds) {
		List<FieldType> fieldTypes = Stream.of(fieldTypeIds)
				.map(fieldTypeId -> this.fieldTypes.getFieldTypeById(fieldTypeId)).collect(Collectors.toList());
		DocumentType documentType = new DocumentType(id, name, fieldTypes);
		documentTypesById.put(documentType.getId(), documentType);
		return documentType;
	}
	
	public Set<String> getDocumentTypeIds() {
		return documentTypesById.keySet();
	}

	public DocumentType getDocumentTypeById(String documentTypeId) throws DocumentTypeNotFoundException {
		DocumentType documentType = documentTypesById.get(documentTypeId);
		if (documentType == null) {
			throw new DocumentTypeNotFoundException(documentTypeId);
		}
		return documentType;
	}
	
	public static final void main(String [] args) throws DocumentTypeNotFoundException, ToJsonException {
		JsonService jsonService = new JsonServiceImpl();
		DocumentTypes documentTypes =  new DocumentTypes();
		
		System.out.println(StringUtils.repeat("=", 80));
		for (String documentTypeId: documentTypes.getDocumentTypeIds()) {
			DocumentType documentType = documentTypes.getDocumentTypeById(documentTypeId);
			Map<String, Object> map = new HashMap<>();
			map.put("document-type-ids:", List.of(documentType.getId()));
			Map<String, String> fields = new HashMap<>();
			for (String fieldTypeId : documentType.getFieldTypeIds()) {
				fields.put(fieldTypeId, "?");
			}
			map.put("fields", fields);
			
			System.out.println(documentType.getName());
			System.out.println(StringUtils.repeat("-", 80));
			System.out.println(jsonService.toJson(map));
			System.out.println(StringUtils.repeat("=", 80));
		}
	}
}