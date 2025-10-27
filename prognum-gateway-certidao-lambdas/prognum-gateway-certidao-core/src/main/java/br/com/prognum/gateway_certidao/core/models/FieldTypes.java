package br.com.prognum.gateway_certidao.core.models;

import java.util.HashMap;
import java.util.Map;

public class FieldTypes {

	public static final String ESTADO_FIELD_TYPE_ID = "estado";
	public static final String CIDADE_FIELD_TYPE_ID = "cidade";
	public static final String RG_FIELD_TYPE_ID = "rg";
	public static final String CPF_FIELD_TYPE_ID = "cpf";
	public static final String CNPJ_FIELD_TYPE_ID = "cnpj";
	public static final String NOME_COMPLETO_FIELD_TYPE_ID = "nome-completo";
	public static final String RAZAO_SOCIAL_FIELD_TYPE_ID = "razao-social";
	public static final String DATA_NASCIMENTO_FIELD_TYPE_ID = "data-nascimento";

	private final Map<String, FieldType> fieldTypes;

	public FieldTypes() {
		fieldTypes = new HashMap<>();

		newFieldType(ESTADO_FIELD_TYPE_ID, "Estado", "Estado");
		newFieldType(CIDADE_FIELD_TYPE_ID, "Cidade", "Cidade");
		newFieldType(RG_FIELD_TYPE_ID, "RG", "Registro Geral");
		newFieldType(CPF_FIELD_TYPE_ID, "CPF", "Cadastro de Pessoa Física");
		newFieldType(CNPJ_FIELD_TYPE_ID, "CNPJ", "Cadastro Nacional de Pessoa Jurídica");
		newFieldType(NOME_COMPLETO_FIELD_TYPE_ID, "Nome Completo", "Nome Completo");
		newFieldType(RAZAO_SOCIAL_FIELD_TYPE_ID, "Razão Social", "Razão Social");
		newFieldType(DATA_NASCIMENTO_FIELD_TYPE_ID, "Data de Nascimento", "Data de Nascimento");
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