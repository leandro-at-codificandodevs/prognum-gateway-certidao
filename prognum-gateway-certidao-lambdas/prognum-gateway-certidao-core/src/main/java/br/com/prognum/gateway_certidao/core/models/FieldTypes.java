package br.com.prognum.gateway_certidao.core.models;

import java.util.HashMap;
import java.util.Map;

public class FieldTypes {

	public static final String CARTORIO_FIELD_TYPE_ID = "cartorio";
	public static final String CEI_FIELD_TYPE_ID = "cei";
	public static final String CEP_FIELD_TYPE_ID = "cep";
	public static final String CIDADE_FIELD_TYPE_ID = "cidade";
	public static final String CNPJ_FIELD_TYPE_ID = "cnpj";
	public static final String COMARCA_FIELD_TYPE_ID = "comarca";
	public static final String CPF_FIELD_TYPE_ID = "cpf";
	public static final String DATA_CASAMENTO_FIELD_TYPE_ID = "data-casamento";
	public static final String DATA_NASCIMENTO_FIELD_TYPE_ID = "data-nascimento";
	public static final String ESTADO_FIELD_TYPE_ID = "estado";
	public static final String FOLHA_FIELD_TYPE_ID = "folha";
	public static final String LIVRO_FIELD_TYPE_ID = "livro";
	public static final String NOME_COMPLETO_FIELD_TYPE_ID = "nome-completo";
	public static final String NOME_MAE_FIELD_TYPE_ID = "nome-mae";
	public static final String NUMERO_INSCRICAO_IMOBILIARIA_FIELD_TYPE_ID = "numero-inscricao-imobiliaria";
	public static final String NUMERO_MATRICULA_IMOVEL_FIELD_TYPE_ID = "numero-matricula-imovel";
	public static final String RAZAO_SOCIAL_FIELD_TYPE_ID = "razao-social";
	public static final String RG_FIELD_TYPE_ID = "rg";
	public static final String TERMO_FIELD_TYPE_ID = "termo";

	private final Map<String, FieldType> fieldTypes;

	public FieldTypes() {
		fieldTypes = new HashMap<>();

		newFieldType(CARTORIO_FIELD_TYPE_ID, "Cartório", "Cartório");
		newFieldType(CEI_FIELD_TYPE_ID, "CEI", "Cadastro Específico do INSS");
		newFieldType(CEP_FIELD_TYPE_ID, "CEP", "Código de Endereço Postal");
		newFieldType(CIDADE_FIELD_TYPE_ID, "Cidade", "Cidade");
		newFieldType(CNPJ_FIELD_TYPE_ID, "CNPJ", "Cadastro Nacional de Pessoa Jurídica");
		newFieldType(COMARCA_FIELD_TYPE_ID, "Comarca", "Comarca");
		newFieldType(CPF_FIELD_TYPE_ID, "CPF", "Cadastro de Pessoa Física");
		newFieldType(DATA_CASAMENTO_FIELD_TYPE_ID, "Data de Casamento", "Data de Casamento");
		newFieldType(DATA_NASCIMENTO_FIELD_TYPE_ID, "Data de Nascimento", "Data de Nascimento");
		newFieldType(ESTADO_FIELD_TYPE_ID, "Estado", "Estado");
		newFieldType(FOLHA_FIELD_TYPE_ID, "Folha", "Folha");
		newFieldType(LIVRO_FIELD_TYPE_ID, "Livro", "Livro");
		newFieldType(NOME_COMPLETO_FIELD_TYPE_ID, "Nome Completo", "Nome Completo");
		newFieldType(NOME_MAE_FIELD_TYPE_ID, "Nome da Mãe", "Nome da Mãe");
		newFieldType(NUMERO_INSCRICAO_IMOBILIARIA_FIELD_TYPE_ID, "Número Inscrição Imobiliária", "Número Inscrição Imobiliária");
		newFieldType(NUMERO_MATRICULA_IMOVEL_FIELD_TYPE_ID, "Número de Matrícula do Imóvel", "Número de Matrícula do Imóvel");
		newFieldType(RAZAO_SOCIAL_FIELD_TYPE_ID, "Razão Social", "Razão Social");
		newFieldType(RG_FIELD_TYPE_ID, "RG", "Registro Geral");
		newFieldType(TERMO_FIELD_TYPE_ID, "Termo", "Termo");
	}
	
	private FieldType newFieldType(String id, String name, String description) {
		FieldType fieldType = new FieldType(id, name, description);
		fieldTypes.put(fieldType.getId(), fieldType);
		return fieldType;
	}

	public FieldType getFieldTypeById(String id) {
		return fieldTypes.get(id);
	}
}