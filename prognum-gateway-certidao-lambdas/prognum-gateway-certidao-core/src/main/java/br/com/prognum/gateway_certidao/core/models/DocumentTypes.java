package br.com.prognum.gateway_certidao.core.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.prognum.gateway_certidao.core.exceptions.DocumentTypeNotFoundException;

public class DocumentTypes {

	public static final String DOCUMENT_TYPE_1 = "cert-executivos-fiscais-justica-estadual-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_2 = "cert-debitos-relativos-tributos-federais-divida-ativa-uniao-pf";
	public static final String DOCUMENT_TYPE_3 = "cert-negativa-debitos-trabalhistas-cndt-pf";
	public static final String DOCUMENT_TYPE_4 = "cert-executivos-fiscais-justica-estadual-1a-instancia-pf-f";
	public static final String DOCUMENT_TYPE_5 = "cert-debitos-relativos-tributos-federais-divida-ativa-uniao-pf-f";
	public static final String DOCUMENT_TYPE_6 = "cert-negativa-debitos-trabalhistas-cndt-pf-f";
	
	private final Map<String, DocumentType> documentTypesById;
	private final FieldTypes fieldTypes;

	public DocumentTypes() {
		fieldTypes = new FieldTypes();
		documentTypesById = new HashMap<>();

		FieldType fieldType1 = fieldTypes.getFieldTypeById(FieldTypes.CPF_FIELD_TYPE_ID);
		FieldType fieldType2 = fieldTypes.getFieldTypeById(FieldTypes.ESTADO_FIELD_TYPE_ID);
		FieldType fieldType3 = fieldTypes.getFieldTypeById(FieldTypes.CIDADE_FIELD_TYPE_ID);
		FieldType fieldType4 = fieldTypes.getFieldTypeById(FieldTypes.DATA_NASCIMENTO_FIELD_TYPE_ID);
		FieldType fieldType5 = fieldTypes.getFieldTypeById(FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		DocumentType documentType1 = new DocumentType(DOCUMENT_TYPE_1,
				"Certidão de Executivos Fiscais - Justiça Estadual (1° instância)",
				"Certidão de Executivos Fiscais - Justiça Estadual (1° instância)",
				List.of(fieldType1, fieldType2, fieldType3, fieldType5));

		DocumentType documentType2 = new DocumentType(DOCUMENT_TYPE_2,
				"Certidão Conjunta de Débitos Relativos a Tributos Federais e a Dívida Ativa da União - Receita Federal",
				"Certidão Conjunta de Débitos Relativos a Tributos Federais e a Dívida Ativa da União - Receita Federal",
				List.of(fieldType1, fieldType2, fieldType3, fieldType4, fieldType5));

		DocumentType documentType3 = new DocumentType(DOCUMENT_TYPE_3,
				"Certidão Negativa de Débitos Trabalhistas - CNDT", "Certidão Negativa de Débitos Trabalhistas - CNDT",
				List.of(fieldType1, fieldType2, fieldType3, fieldType5));

		DocumentType documentType4 = new DocumentType(DOCUMENT_TYPE_4,
				"Certidão de Executivos Fiscais - Justiça Estadual (1° instância)*",
				"Certidão de Executivos Fiscais - Justiça Estadual (1° instância)*",
				List.of(fieldType1, fieldType2, fieldType3, fieldType5));

		DocumentType documentType5 = new DocumentType(DOCUMENT_TYPE_5,
				"Certidão Conjunta de Débitos Relativos a Tributos Federais e a Dívida Ativa da União - Receita Federal*",
				"Certidão Conjunta de Débitos Relativos a Tributos Federais e a Dívida Ativa da União - Receita Federal*",
				List.of(fieldType1, fieldType2, fieldType3, fieldType4, fieldType5));

		DocumentType documentType6 = new DocumentType(DOCUMENT_TYPE_6,
				"Certidão Negativa de Débitos Trabalhistas - CNDT*", "Certidão Negativa de Débitos Trabalhistas - CNDT*",
				List.of(fieldType1, fieldType2, fieldType3, fieldType5));


		documentTypesById.put(documentType1.getId(), documentType1);
		documentTypesById.put(documentType2.getId(), documentType2);
		documentTypesById.put(documentType3.getId(), documentType3);
		documentTypesById.put(documentType4.getId(), documentType4);
		documentTypesById.put(documentType5.getId(), documentType5);
		documentTypesById.put(documentType6.getId(), documentType6);
	}

	public DocumentType getDocumentTypeById(String documentTypeId) throws DocumentTypeNotFoundException {
		DocumentType documentType = documentTypesById.get(documentTypeId);
		if (documentType == null) {
			throw new DocumentTypeNotFoundException(documentTypeId);
		}
		return documentType;
	}
}