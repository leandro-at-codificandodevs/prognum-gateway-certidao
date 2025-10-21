package br.com.prognum.gateway_certidao.core.models;

import java.util.List;

import br.com.prognum.gateway_certidao.core.exceptions.FieldTypeNotFoundException;
import lombok.Value;

@Value
public class DocumentType {
	private final String id;
	private final String name;
	private final String description;
	private final List<FieldType> fieldTypes;

	public FieldType getFieldById(String fieldTypeId) throws FieldTypeNotFoundException {
		FieldType fieldType = fieldTypes.stream().filter(f -> f.getId() == fieldTypeId).findAny()
				.orElseThrow(() -> new FieldTypeNotFoundException(fieldTypeId));

		return fieldType;
	}
}
