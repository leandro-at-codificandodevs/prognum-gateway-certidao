package br.com.prognum.gateway_certidao.core.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.prognum.gateway_certidao.core.exceptions.DocumentTypeNotFoundException;

public class DocumentTypes {

	public static final String DOCUMENT_TYPE_ID_1 = "cert-acoes-civis-justica-federal-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_2 = "cert-acoes-civis-justica-federal-2a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_3 = "cert-acoes-criminais-justica-federal-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_4 = "cert-acoes-criminais-justica-federal-2a-instancia-pf";

	private final Map<String, DocumentType> documentTypesById;
	private final FieldTypes fieldTypes;

	public DocumentTypes() {
		fieldTypes = new FieldTypes();

		FieldType fieldType1 = fieldTypes.getFieldTypeById(FieldTypes.ESTADO_FIELD_TYPE_ID);
		FieldType fieldType2 = fieldTypes.getFieldTypeById(FieldTypes.CIDADE_FIELD_TYPE_ID);
		FieldType fieldType3 = fieldTypes.getFieldTypeById(FieldTypes.CPF_FIELD_TYPE_ID);
		FieldType fieldType4 = fieldTypes.getFieldTypeById(FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);
		FieldType fieldType5 = fieldTypes.getFieldTypeById(FieldTypes.DATA_NASCIMENTO_FIELD_TYPE_ID);

		documentTypesById = new HashMap<>();

		DocumentType documentType1 = new DocumentType(DOCUMENT_TYPE_ID_1,
				"Certidão de Distribuição de Ações Cíveis - Justiça Federal - 1a instância - PF",
				List.of(fieldType1, fieldType2, fieldType3, fieldType4));
		documentTypesById.put(documentType1.getId(), documentType1);

		DocumentType documentType2 = new DocumentType(DOCUMENT_TYPE_ID_2,
				"Certidão de Distribuição de Ações Criminais - Justiça Federal - 2a instância - PF",
				List.of(fieldType1, fieldType2, fieldType3, fieldType4));
		documentTypesById.put(documentType2.getId(), documentType2);

		DocumentType documentType3 = new DocumentType(DOCUMENT_TYPE_ID_3,
				"Certidão de Distribuição de Ações Criminais - Justiça Federal - 1a instância - PF",
				List.of(fieldType1, fieldType2, fieldType3, fieldType4));
		documentTypesById.put(documentType3.getId(), documentType3);

		DocumentType documentType4 = new DocumentType(DOCUMENT_TYPE_ID_4,
				"Certidão de Distribuição de Ações Criminais - Justiça Federal - 2a instância - PF",
				List.of(fieldType1, fieldType2, fieldType3, fieldType4));
		documentTypesById.put(documentType4.getId(), documentType4);
	}

	public DocumentType getDocumentTypeById(String documentTypeId) throws DocumentTypeNotFoundException {
		DocumentType documentType = documentTypesById.get(documentTypeId);
		if (documentType == null) {
			throw new DocumentTypeNotFoundException(documentTypeId);
		}
		return documentType;
	}
}