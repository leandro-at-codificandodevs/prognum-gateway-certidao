package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class InvalidDocumentGroupRequestException extends Exception {
	public InvalidDocumentGroupRequestException(String field) {
		super(String.format("%s", field));
	}
}
