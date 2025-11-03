package br.com.prognum.gateway_certidao.core.models;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.prognum.gateway_certidao.core.exceptions.FieldTypeNotFoundException;
import lombok.Value;

@Value
public class DocumentType {
	private final String id;
	private final String name;

	private final List<FieldType> fieldTypes;
	
	@JsonIgnore
	private final States states;
	
	public List<String> getStateAcronymns() {
		return states.getStates().stream().map(state -> state.getAcronymn()).sorted().toList();
	}
	
	@JsonIgnore
	public Set<String> getFieldTypeIds() {
		return fieldTypes.stream().map(fieldType -> fieldType.getId()).collect(Collectors.toSet());
	}

	public FieldType getFieldById(String fieldTypeId) throws FieldTypeNotFoundException {
		FieldType fieldType = fieldTypes.stream().filter(f -> f.getId() == fieldTypeId).findAny()
				.orElseThrow(() -> new FieldTypeNotFoundException(fieldTypeId));

		return fieldType;
	}
}
