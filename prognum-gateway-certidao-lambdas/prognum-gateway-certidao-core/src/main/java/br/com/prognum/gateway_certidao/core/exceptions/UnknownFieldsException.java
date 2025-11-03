package br.com.prognum.gateway_certidao.core.exceptions;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class UnknownFieldsException extends Exception {
	public UnknownFieldsException(List<String> fields) {
		super(String.format("Campos desconhecidos: %s", fields.stream().collect(Collectors.joining(", "))));
	}
}
