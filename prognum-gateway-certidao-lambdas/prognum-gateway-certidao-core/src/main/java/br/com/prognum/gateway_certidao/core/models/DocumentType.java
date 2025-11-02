package br.com.prognum.gateway_certidao.core.models;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import br.com.prognum.gateway_certidao.core.exceptions.FieldTypeNotFoundException;
import lombok.Value;

@Value
public class DocumentType {
	private final String id;
	private final String name;
	private final List<FieldType> fieldTypes;
	private final States states;
	
	public Set<String> getFieldTypeIds() {
		return fieldTypes.stream().map(fieldType -> fieldType.getId()).collect(Collectors.toSet());
	}

	public FieldType getFieldById(String fieldTypeId) throws FieldTypeNotFoundException {
		FieldType fieldType = fieldTypes.stream().filter(f -> f.getId() == fieldTypeId).findAny()
				.orElseThrow(() -> new FieldTypeNotFoundException(fieldTypeId));

		return fieldType;
	}
}
