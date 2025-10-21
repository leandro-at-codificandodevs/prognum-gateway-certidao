package br.com.prognum.gateway_certidao.core.models;

import java.util.HashMap;
import java.util.Map;

public class FieldTypes {

	public static final String CPF_FIELD_TYPE_ID = "cpf";
	public static final String ESTADO_FIELD_TYPE_ID = "estado";
	public static final String CIDADE_FIELD_TYPE_ID = "cidade";
	public static final String DATA_NASCIMENTO_FIELD_TYPE_ID = "data-nascimento";
	public static final String NOME_COMPLETO_FIELD_TYPE_ID = "nome-completo";

	private final Map<String, FieldType> fieldTypes;

	public FieldTypes() {
		fieldTypes = new HashMap<>();

		FieldType fieldType1 = new FieldType(CPF_FIELD_TYPE_ID, "CPF", "Cadastro de pessoa física");
		fieldTypes.put(CPF_FIELD_TYPE_ID, fieldType1);

		FieldType fieldType2 = new FieldType(ESTADO_FIELD_TYPE_ID, "Estado", "Estado");
		fieldTypes.put(ESTADO_FIELD_TYPE_ID, fieldType2);

		FieldType fieldType3 = new FieldType(CIDADE_FIELD_TYPE_ID, "Cidade", "Cidade");
		fieldTypes.put(CIDADE_FIELD_TYPE_ID, fieldType3);

		FieldType fieldType4 = new FieldType(DATA_NASCIMENTO_FIELD_TYPE_ID, "Data de Nascimento", "Data de Nascimento");
		fieldTypes.put(DATA_NASCIMENTO_FIELD_TYPE_ID, fieldType4);

		FieldType fieldType5 = new FieldType(NOME_COMPLETO_FIELD_TYPE_ID, "Nome Completo", "Nome Completo");
		fieldTypes.put(NOME_COMPLETO_FIELD_TYPE_ID, fieldType5);
	}

	public FieldType getFieldTypeById(String id) {
		return fieldTypes.get(id);
	}
}