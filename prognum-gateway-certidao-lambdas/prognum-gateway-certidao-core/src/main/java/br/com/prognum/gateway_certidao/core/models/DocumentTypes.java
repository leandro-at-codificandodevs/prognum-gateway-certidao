package br.com.prognum.gateway_certidao.core.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.prognum.gateway_certidao.core.exceptions.DocumentTypeNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import software.amazon.awssdk.utils.StringUtils;

public class DocumentTypes {
	public static final String DOCUMENT_TYPE_ID_1 = "cert-acoes-civeis-justica-federal-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_2 = "cert-acoes-civeis-justica-federal-1a-instancia-pj";
	public static final String DOCUMENT_TYPE_ID_3 = "cert-acoes-criminais-justica-federal-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_4 = "cert-acoes-criminais-justica-federal-1a-instancia-pj";
	public static final String DOCUMENT_TYPE_ID_5 = "cert-negativa-debitos-trabalhistas-pf";
	public static final String DOCUMENT_TYPE_ID_6 = "cert-negativa-debitos-trabalhistas-pj";
	public static final String DOCUMENT_TYPE_ID_7 = "cert-acoes-civeis-justica-estadual-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_8 = "cert-acoes-civeis-justica-estadual-1a-instancia-pj";
	public static final String DOCUMENT_TYPE_ID_9 = "cert-acoes-criminais-justica-estadual-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_10 = "cert-acoes-criminais-justica-estadual-1a-instancia-pj";
	public static final String DOCUMENT_TYPE_ID_11 = "cert-inventarios-arrolamentos-testamentos-judicial-pf";
	public static final String DOCUMENT_TYPE_ID_12 = "cert-inventarios-arrolamentos-testamentos-judicial-pj";
	public static final String DOCUMENT_TYPE_ID_13 = "cert-acoes-trabalhias-tribunal-regional-trabalho-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_14 = "cert-acoes-trabalhias-tribunal-regional-trabalho-1a-instancia-pj";
	public static final String DOCUMENT_TYPE_ID_15 = "cert-regularidade-fiscal-estadual-pf";
	public static final String DOCUMENT_TYPE_ID_16 = "cert-regularidade-fiscal-estadual-pf";
	public static final String DOCUMENT_TYPE_ID_17 = "cert-inscricao-estadual-produtor-rural-pf";
	public static final String DOCUMENT_TYPE_ID_18 = "cert-inscricao-estadual-produtor-rural-pj";
	public static final String DOCUMENT_TYPE_ID_19 = "cert-regularidade-fiscal-municipal-pf";
	public static final String DOCUMENT_TYPE_ID_20 = "cert-regularidade-fiscal-municipal-pj";
	public static final String DOCUMENT_TYPE_ID_21 = "cert-execucoes-criminais-justica-estadual-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_22 = "cert-execucoes-criminais-justica-estadual-1a-instancia-pj";
	public static final String DOCUMENT_TYPE_ID_23 = "cert-regularidade-fgts-crf-pf";
	public static final String DOCUMENT_TYPE_ID_24 = "cert-regularidade-fgts-crf-pj";
	public static final String DOCUMENT_TYPE_ID_25 = "cert-execucoes-fiscais-justica-estadual-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_26 = "cert-execucoes-fiscais-justica-estadual-1a-instancia-pj";

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
		
		newDocumentType(DOCUMENT_TYPE_ID_7, "Certidão de Distribuição de Ações Cíveis - Justiça Estadual - 1a instância - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID, FieldTypes.NOME_MAE_FIELD_TYPE_ID, FieldTypes.DATA_NASCIMENTO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_8, "Certidão de Distribuição de Ações Cíveis - Justiça Estadual - 1a instância - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);
		
		newDocumentType(DOCUMENT_TYPE_ID_9, "Certidão de Distribuição de Ações Criminais - Justiça Estadual - 1a instância - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_10, "Certidão de Distribuição de Ações Criminais - Justiça Estadual - 1a instância - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);
		
		newDocumentType(DOCUMENT_TYPE_ID_11, "Certidões de Inventários, Arrolamentos e Testamentos - Judicial - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_12, "Certidões de Inventários, Arrolamentos e Testamentos - Judicial - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);
		
		newDocumentType(DOCUMENT_TYPE_ID_13, "Certidão de Distribuição de Ações Trabalhistas - Tribunal Regional do Trabalho - 1a instância - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_14, "Certidão de Distribuição de Ações Trabalhistas - Tribunal Regional do Trabalho - 1a instância - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);
		
		newDocumentType(DOCUMENT_TYPE_ID_15, "Certidão de Regularidade Fiscal Estadual - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_16, "Certidão de Regularidade Fiscal Estadual - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);
		
		newDocumentType(DOCUMENT_TYPE_ID_17, "Certidão Inscrição Estadual do Produtor Rural - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_18, "Certidão Inscrição Estadual do Produtor Rural - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);
		
		newDocumentType(DOCUMENT_TYPE_ID_19, "Certidão de Regularidade Fiscal Municipal - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);

		newDocumentType(DOCUMENT_TYPE_ID_20, "Certidão de Regularidade Fiscal Municipal - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);
		
		newDocumentType(DOCUMENT_TYPE_ID_21, "Certidão de Execuções Criminais - Justiça Estadual - 1a instância - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID, FieldTypes.NOME_MAE_FIELD_TYPE_ID, FieldTypes.DATA_NASCIMENTO_FIELD_TYPE_ID);
	
		newDocumentType(DOCUMENT_TYPE_ID_22, "Certidão de Execuções Criminais - Justiça Estadual - 1a instância - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);
		
		newDocumentType(DOCUMENT_TYPE_ID_23, "Certificado de Regularidade do FGTS - CRF -PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.CEI_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);
	
		newDocumentType(DOCUMENT_TYPE_ID_24, "Certificado de Regularidade do FGTS - CRF - PJ",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CNPJ_FIELD_TYPE_ID,
				FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID);
		
		newDocumentType(DOCUMENT_TYPE_ID_25, "Certidão de Execuções Fiscais - Justiça Estadual - 1a instância - PF",
				FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID, FieldTypes.CPF_FIELD_TYPE_ID,
				FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID);
	
		newDocumentType(DOCUMENT_TYPE_ID_26, "Certidão de Execuções Fiscais - Justiça Estadual - 1a instância - PJ",
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

	@JsonProperty
	public Set<String> getDocumentTypeIds() {
		return documentTypesById.keySet();
	}
	
	@JsonProperty
	public Set<DocumentType> getDocumentTypes() {
		return this.documentTypesById.values().stream().collect(Collectors.toSet());
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