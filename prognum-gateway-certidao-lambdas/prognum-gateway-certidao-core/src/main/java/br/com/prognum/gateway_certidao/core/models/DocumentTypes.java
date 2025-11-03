package br.com.prognum.gateway_certidao.core.models;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.prognum.gateway_certidao.core.exceptions.DocumentTypeNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.InternalServerException;
import br.com.prognum.gateway_certidao.core.exceptions.StateNotFoundException;
import br.com.prognum.gateway_certidao.core.exceptions.ToJsonException;
import br.com.prognum.gateway_certidao.core.services.JsonService;
import br.com.prognum.gateway_certidao.core.services.JsonServiceImpl;
import br.com.prognum.gateway_certidao.core.services.StateService;
import br.com.prognum.gateway_certidao.core.services.StateServiceImpl;

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
	public static final String DOCUMENT_TYPE_ID_16 = "cert-regularidade-fiscal-estadual-pj";
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
	public static final String DOCUMENT_TYPE_ID_27 = "cert-debitos-tributos-federais-divida-ativa-uniao-receita-federal-pf";
	public static final String DOCUMENT_TYPE_ID_28 = "cert-debitos-tributos-federais-divida-ativa-uniao-receita-federal-pj";
	public static final String DOCUMENT_TYPE_ID_29 = "cert-inventarios-arrolamentos-testamentos-extrajudicial-pf";
	public static final String DOCUMENT_TYPE_ID_30 = "cert-negativa-debitos-tributos-imobiliarios-imovel";
	public static final String DOCUMENT_TYPE_ID_31 = "cert-atualizada-matricula-imovel-imovel";
	public static final String DOCUMENT_TYPE_ID_32 = "cert-negativa-debitos-tributarios-divida-ativa-pge-pf";
	public static final String DOCUMENT_TYPE_ID_33 = "cert-negativa-debitos-tributarios-divida-ativa-pge-pj";
	public static final String DOCUMENT_TYPE_ID_34 = "cert-acoes-trabalhias-tribunal-regional-trabalho-processos-judiciais-eletronicos-1a-instancia-pf";
	public static final String DOCUMENT_TYPE_ID_35 = "cert-acoes-trabalhias-tribunal-regional-trabalho-processos-judiciais-eletronicos-1a-instancia-pj";
	public static final String DOCUMENT_TYPE_ID_36 = "cert-2a-via-nascimento-pf";
	public static final String DOCUMENT_TYPE_ID_37 = "cert-2a-via-casamento-pf";

	private States states;

	private final FieldTypes fieldTypes;

	private final Map<String, DocumentType> documentTypesById;

	public DocumentTypes(StateService stateService) {
		try {
			states = stateService.getStates();

			fieldTypes = new FieldTypes();

			documentTypesById = new LinkedHashMap<>();

			newDocumentType(DOCUMENT_TYPE_ID_1,
					"Certidão de Distribuição de Ações Cíveis - Justiça Federal - 1a instância - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_2,
					"Certidão de Distribuição de Ações Criminais - Justiça Federal - 1a instância - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_3,
					"Certidão de Distribuição de Ações Criminais - Justiça Federal - 1a instância - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_4,
					"Certidão de Distribuição de Ações Criminais - Justiça Federal - 1a instância - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_5, "Certidão Negativa de Débitos Trabalhistas - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_6, "Certidão Negativa de Débitos Trabalhistas - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_7,
					"Certidão de Distribuição de Ações Cíveis - Justiça Estadual - 1a instância - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID,
							FieldTypes.NOME_MAE_FIELD_TYPE_ID, FieldTypes.DATA_NASCIMENTO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_8,
					"Certidão de Distribuição de Ações Cíveis - Justiça Estadual - 1a instância - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_9,
					"Certidão de Distribuição de Ações Criminais - Justiça Estadual - 1a instância - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_10,
					"Certidão de Distribuição de Ações Criminais - Justiça Estadual - 1a instância - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_11, "Certidões de Inventários, Arrolamentos e Testamentos - Judicial - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_12, "Certidões de Inventários, Arrolamentos e Testamentos - Judicial - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_13,
					"Certidão de Distribuição de Ações Trabalhistas - Tribunal Regional do Trabalho - 1a instância - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_14,
					"Certidão de Distribuição de Ações Trabalhistas - Tribunal Regional do Trabalho - 1a instância - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_15, "Certidão de Regularidade Fiscal Estadual - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_16, "Certidão de Regularidade Fiscal Estadual - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_17, "Certidão Inscrição Estadual do Produtor Rural - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_18, "Certidão Inscrição Estadual do Produtor Rural - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_19, "Certidão de Regularidade Fiscal Municipal - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_20, "Certidão de Regularidade Fiscal Municipal - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_21,
					"Certidão de Execuções Criminais - Justiça Estadual - 1a instância - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID,
							FieldTypes.NOME_MAE_FIELD_TYPE_ID, FieldTypes.DATA_NASCIMENTO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_22,
					"Certidão de Execuções Criminais - Justiça Estadual - 1a instância - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_23, "Certificado de Regularidade do FGTS - CRF -PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.CEI_FIELD_TYPE_ID,
							FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_24, "Certificado de Regularidade do FGTS - CRF - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_25, "Certidão de Execuções Fiscais - Justiça Estadual - 1a instância - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_26, "Certidão de Execuções Fiscais - Justiça Estadual - 1a instância - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_27,
					"Certidão de Débitos Relativos a Tributos Federais e a Dívida Ativa da União - Receita Federal - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID,
							FieldTypes.DATA_NASCIMENTO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_28,
					"Certidão de Débitos Relativos a Tributos Federais e a Dívida Ativa da União - Receita Federal - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_29,
					"Certidões de Inventários, Arrolamentos e Testamentos - Extrajudicial - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID,
							FieldTypes.CARTORIO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_30, "Certidão Negativa de Débitos de Tributos Imobiliários - Imóvel",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.NUMERO_MATRICULA_IMOVEL_FIELD_TYPE_ID,
							FieldTypes.NUMERO_INSCRICAO_IMOBILIARIA_FIELD_TYPE_ID, FieldTypes.CEP_FIELD_TYPE_ID,
							FieldTypes.COMARCA_FIELD_TYPE_ID, FieldTypes.CARTORIO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_31, "Certidão Atualizada de Matrícula de Imóvel - Imóvel",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.NUMERO_MATRICULA_IMOVEL_FIELD_TYPE_ID, FieldTypes.CEP_FIELD_TYPE_ID,
							FieldTypes.COMARCA_FIELD_TYPE_ID, FieldTypes.CARTORIO_FIELD_TYPE_ID),
					states.getStatesByAcronymn("AC", "GO", "MA", "PI", "RJ", "SP"));

			newDocumentType(DOCUMENT_TYPE_ID_32,
					"Certidão Negativa de Débitos Tributários Inscritos em Dívida Ativa - Procuradoria Geral Do Estado - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states.getStatesByAcronymn("AC", "GO", "MA", "PI", "RJ", "SP"));

			newDocumentType(DOCUMENT_TYPE_ID_33,
					"Certidão Negativa de Débitos Tributários Inscritos em Dívida Ativa - Procuradoria Geral Do Estado - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states.getStatesByAcronymn("AC", "GO", "MA", "PI", "RJ", "SP"));

			newDocumentType(DOCUMENT_TYPE_ID_34,
					"Certidão de Distribuição de Ações Trabalhistas - Tribunal Regional do Trabalho - Processos Judiciais Eletrônicos - 1a instância - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID),
					states.getStatesByAcronymn("AP", "CE", "PA", "RJ", "SP"));

			newDocumentType(DOCUMENT_TYPE_ID_35,
					"Certidão de Distribuição de Ações Trabalhistas - Tribunal Regional do Trabalho - Processos Judiciais Eletrônicos - 1a instância - PJ",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CNPJ_FIELD_TYPE_ID, FieldTypes.RAZAO_SOCIAL_FIELD_TYPE_ID),
					states.getStatesByAcronymn("AP", "CE", "PA", "RJ", "SP"));

			newDocumentType(DOCUMENT_TYPE_ID_36, "2a via de Certidão de Nascimento - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID,
							FieldTypes.DATA_NASCIMENTO_FIELD_TYPE_ID, FieldTypes.CARTORIO_FIELD_TYPE_ID,
							FieldTypes.LIVRO_FIELD_TYPE_ID, FieldTypes.FOLHA_FIELD_TYPE_ID,
							FieldTypes.TERMO_FIELD_TYPE_ID),
					states);

			newDocumentType(DOCUMENT_TYPE_ID_37, "2a via de Certidão de Casamento - PF",
					List.of(FieldTypes.ESTADO_FIELD_TYPE_ID, FieldTypes.CIDADE_FIELD_TYPE_ID,
							FieldTypes.CPF_FIELD_TYPE_ID, FieldTypes.NOME_COMPLETO_FIELD_TYPE_ID,
							FieldTypes.DATA_CASAMENTO_FIELD_TYPE_ID, FieldTypes.CARTORIO_FIELD_TYPE_ID,
							FieldTypes.LIVRO_FIELD_TYPE_ID, FieldTypes.FOLHA_FIELD_TYPE_ID,
							FieldTypes.TERMO_FIELD_TYPE_ID),
					states);

		} catch (StateNotFoundException e) {
			throw new InternalServerException(e);
		}
	}

	private DocumentType newDocumentType(String id, String name, List<String> fieldTypeIds, States states) {
		List<FieldType> fieldTypes = fieldTypeIds.stream()
				.map(fieldTypeId -> this.fieldTypes.getFieldTypeById(fieldTypeId)).collect(Collectors.toList());
		if (documentTypesById.containsKey(id)) {
			throw new InternalServerException(String.format("Tipo de Documento duplicado: %s", id));
		}
		DocumentType documentType = new DocumentType(id, name, fieldTypes, states);
		documentTypesById.put(documentType.getId(), documentType);
		return documentType;
	}

	@JsonProperty
	public List<String> getDocumentTypeIds() {
		return documentTypesById.keySet().stream().collect(Collectors.toList());
	}

	@JsonProperty
	public List<DocumentType> getDocumentTypes() {
		return documentTypesById.values().stream().collect(Collectors.toList());
	}

	public DocumentType getDocumentTypeById(String documentTypeId) throws DocumentTypeNotFoundException {
		DocumentType documentType = documentTypesById.get(documentTypeId);
		if (documentType == null) {
			throw new DocumentTypeNotFoundException(documentTypeId);
		}
		return documentType;
	}
	
	public static final void main(String [] args) throws ToJsonException {
		DocumentTypes types = new DocumentTypes(new StateServiceImpl());
		JsonService jsonService = new JsonServiceImpl();
		System.out.println(jsonService.toJson(types.getDocumentTypes()));
	}
}