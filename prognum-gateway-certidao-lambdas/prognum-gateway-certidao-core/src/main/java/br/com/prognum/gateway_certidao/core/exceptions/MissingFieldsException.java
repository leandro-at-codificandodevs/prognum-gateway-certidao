package br.com.prognum.gateway_certidao.core.exceptions;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class MissingFieldsException  extends Exception {
	public MissingFieldsException(List<String> fields) {
		super(String.format("Campo obrigatório ausente: %s", fields.stream().collect(Collectors.joining(", "))));
	}
}
