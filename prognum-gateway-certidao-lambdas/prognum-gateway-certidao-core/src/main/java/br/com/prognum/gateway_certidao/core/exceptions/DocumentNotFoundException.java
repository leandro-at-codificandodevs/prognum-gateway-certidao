package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class DocumentNotFoundException extends Exception {
	public DocumentNotFoundException(String id) {
		super(String.format("O documento %s não foi encontrado", id));
	}
}
