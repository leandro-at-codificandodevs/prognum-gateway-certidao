package br.com.prognum.gateway_certidao.core.exceptions;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class EmptyFieldsException  extends Exception {
	public EmptyFieldsException(List<String> fields) {
		super(String.format("Campo obrigatório vazio: %s", fields.stream().collect(Collectors.joining(", "))));
	}
}
