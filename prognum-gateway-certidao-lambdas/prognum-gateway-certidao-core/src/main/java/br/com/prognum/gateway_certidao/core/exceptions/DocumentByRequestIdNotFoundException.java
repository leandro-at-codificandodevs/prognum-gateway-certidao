package br.com.prognum.gateway_certidao.core.exceptions;

@SuppressWarnings("serial")
public class DocumentByRequestIdNotFoundException extends Exception {
	public DocumentByRequestIdNotFoundException(String requestId) {
		super(String.format("O documento para requisição cujo ID é %s não foi encontrado", requestId));
	}
}
