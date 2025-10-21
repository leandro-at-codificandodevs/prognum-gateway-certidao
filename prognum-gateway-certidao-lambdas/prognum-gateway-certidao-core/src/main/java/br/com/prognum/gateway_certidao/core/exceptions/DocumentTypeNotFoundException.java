package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class DocumentTypeNotFoundException extends Exception {
	public DocumentTypeNotFoundException(String documentTypeId) {
		super(String.format("Tipo de documento %s não encontrado", documentTypeId));

	}
}
