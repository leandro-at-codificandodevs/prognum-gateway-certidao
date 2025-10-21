package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class UnknownFieldException extends Exception {
	public UnknownFieldException(String field) {
		super(String.format("Campo %s desconhecido", field));
	}
}
