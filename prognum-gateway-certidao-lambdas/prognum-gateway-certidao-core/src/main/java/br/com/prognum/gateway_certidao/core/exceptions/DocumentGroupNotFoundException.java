package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class DocumentGroupNotFoundException extends Exception {
	public DocumentGroupNotFoundException(String id) {
		super(String.format("O grupo de documento %s não foi encontrado", id));
	}
}
