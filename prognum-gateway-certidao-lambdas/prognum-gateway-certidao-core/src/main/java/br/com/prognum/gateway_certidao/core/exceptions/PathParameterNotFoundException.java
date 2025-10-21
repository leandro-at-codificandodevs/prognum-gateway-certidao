package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class PathParameterNotFoundException extends Exception {
	public PathParameterNotFoundException(String parameterName) {
		super(String.format("Path '%s' não encontrado ou inválido", parameterName));
	}
}
