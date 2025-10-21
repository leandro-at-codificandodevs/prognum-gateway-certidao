package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class FieldTypeNotFoundException extends Exception {
	public FieldTypeNotFoundException(String fieldTypeId) {
		super(String.format("Tipo de campo %s não encontrado", fieldTypeId));

	}
}
